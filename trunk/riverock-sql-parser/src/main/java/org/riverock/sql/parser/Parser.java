/*
 * org.riverock.sql -- Classes for tracking database changes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.sql.parser;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.schema.sql.*;
import org.riverock.schema.sql.types.UnionTypeType;

public class Parser {
    private static Logger cat = Logger.getLogger(Parser.class);

    private Tokenizer tTokenizer;
    private String sTable;
    private String sToken;
    private Object oData;
    private int iType;
    private int iToken;

    public String sql;
    public int typeStatement = 0;

    public SelectType select = null;
    public InsertType insert = null;
    public UpdateType update = null;
    public DeleteType delete = null;

    public DependenceType depend = new DependenceType();

    private void getSourceTable()
        throws Exception {
        switch (typeStatement) {
            case Parser.SELECT:
                if (select == null)
                    return;

                getSourceFromTable(select);
                if (select.getExpCondition() != null)
                    getSourceFromExpression(select.getExpCondition());

                break;
            case Parser.INSERT:
                if (insert.getSelect() != null)
                    getSourceFromTable(insert.getSelect());
//                depend.getTarget().clearItem();
                depend.getTarget().removeAllItem();
                depend.getTarget().addItem(insert.getTable().getTableName());
                break;

            case Parser.UPDATE:
                if (update.getExpression() != null)
                    getSourceFromExpression(update.getExpression());
//                depend.getTarget().clearItem();
                depend.getTarget().removeAllItem();
                depend.getTarget().addItem(update.getTable().getTableName());
                break;

            case Parser.DELETE:
                if (delete.getExpression() != null)
                    getSourceFromExpression(delete.getExpression());
//                depend.getTarget().clearItem();
                depend.getTarget().removeAllItem();
                depend.getTarget().addItem(delete.getTable().getTableName());
                break;

            default:
                throw new Exception("Error parse sql");
        }
    }

    private void getSourceFromExpression(ExpressionType expCondition) {
        if (expCondition == null)
            return;

        if (expCondition.getSelect() != null)
            getSourceFromTable(expCondition.getSelect());

        if (expCondition.getExpArg1() != null)
            getSourceFromExpression(expCondition.getExpArg1());

        if (expCondition.getExpArg2() != null)
            getSourceFromExpression(expCondition.getExpArg2());

    }

    private boolean isExists(SqlNameListType list, SqlNameType name) {
        if (name == null || list == null)
            return false;

        for (int j = 0; j < list.getItemCount(); j++) {
            if (name.getOriginName().equals(list.getItem(j).getOriginName()))
                return true;
        }
        return false;
    }

    private void getSourceFromTable(SelectType select_) {
        for (int i = 0; i < select_.getTableFilterCount(); i++) {
            TableFilterType f = select_.getTableFilter(i);
            TableType table = f.getTable();
            if (table == null)
                continue;

            if (table.getTableName() != null) {
                if (!isExists(depend.getSource(), table.getTableName()))
                    depend.getSource().addItem(table.getTableName());
            }
            else if (table.getSubQuery() != null) {
                getSourceFromTable(table.getSubQuery().getSelect());
            }
            else
                continue;

        }
    }

    /**
     * Constructor declaration
     *
     * @param statement
     */
    private Parser(String statement)
        throws Exception {
        depend.setSource(new SqlNameListType());
        depend.setTarget(new SqlNameListType());
        sql = statement;
        try {
            tTokenizer = new Tokenizer(statement);
            while (true) {
                tTokenizer.setPartMarker();

                String sToken = tTokenizer.getString();

                if (sToken.length() == 0) {
                    break;
                }

                Integer command = hCommands.get(sToken);

                if (command == null) {
                    String errorString = "UNEXPECTED_TOKEN " + sToken;
                    cat.error(errorString);
                    throw new Exception(errorString);
                }

                typeStatement = command;

                switch (typeStatement) {

                    case SELECT:
                        select = parseSelect();
                        break;

                    case INSERT:
                        insert = processInsert();
                        break;

                    case UPDATE:
                        update = processUpdate();
                        break;

                    case DELETE:
                        delete = processDelete();
                        break;
                }
                getSourceTable();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            String s = "GENERAL_ERROR " + " " + e;

            throw new Exception(s + " in statement [" + statement + "]");
        }
        catch (java.lang.OutOfMemoryError e) {
            e.printStackTrace();

            throw new Exception("out of memory GENERAL_ERROR");
        }
    }

    /**
     * Method declaration
     *
     * @throws Exception
     */
    UpdateType processUpdate()
        throws Exception {
        UpdateType updateTemp = new UpdateType();

        String token = tTokenizer.getString();
        TableType table = ServiceClass.getInstance(ServiceClass.getInstance(token, false));
        updateTemp.setTable(table);

        TableFilterType filter = TableService.getInstance(table, null, false);

        tTokenizer.getThis("SET");

        ArrayList<ExpressionType> eColumn = new ArrayList<ExpressionType>();

        token = null;

        do {
            tTokenizer.getString();
//            int i = ServiceClass.getColumnNr( table, tTokenizer.getString() );
//            vColumn.add( new Integer( i ) );

            tTokenizer.getThis("=");

            ExpressionType e = parseExpression();

            ExpressionService.resolve(e, filter);
            eColumn.add(e);

            token = tTokenizer.getString();
        }
        while (token.equals(","));

        ExpressionType eCondition = null;

        if (token.equals("WHERE")) {
            eCondition = parseExpression();

            ExpressionService.resolve(eCondition, filter);
            TableService.setCondition(filter, eCondition);
        }
        else {
            tTokenizer.back();
        }
        updateTemp.setExpression(eCondition);

        return updateTemp;
    }

    /**
     * Method declaration
     *
     * @throws Exception
     */
    DeleteType processDelete()
        throws Exception {
        DeleteType deleteTemp = new DeleteType();
        tTokenizer.getThis("FROM");

        String token = tTokenizer.getString();
        TableType table = ServiceClass.getInstance(ServiceClass.getInstance(token, false));
        deleteTemp.setTable(table);

        TableFilterType filter = TableService.getInstance(table, null, false);

        token = tTokenizer.getString();

        ExpressionType eCondition = null;

        if (token.equals("WHERE")) {
            eCondition = parseExpression();

            ExpressionService.resolve(eCondition, filter);
            TableService.setCondition(filter, eCondition);
        }
        else {
            tTokenizer.back();
        }
        deleteTemp.setExpression(eCondition);

        return deleteTemp;
    }

    /**
     * Method declaration
     */
    InsertType processInsert() throws Exception {

        tTokenizer.getThis("INTO");

        InsertType insertTemp = new InsertType();

        String token = tTokenizer.getString();
        TableType t = ServiceClass.getInstance(ServiceClass.getInstance(token, false));
        insertTemp.setTable(t);

        token = tTokenizer.getString();

        ArrayList<String> vcolumns = null;

        if (token.equals("(")) {
            vcolumns = new ArrayList<String>();

            while (true) {
                vcolumns.add(tTokenizer.getString());

                token = tTokenizer.getString();

                if (token.equals(",")) {
                    continue;
                }

                if (token.equals(")")) {
                    break;
                }

                throw new Exception("UNEXPECTED_TOKEN " + token);
            }

            token = tTokenizer.getString();
        }

        int count = 0;
        int len;

        if (vcolumns == null) {
            len = t.getVisibleColumns();
        }
        else {
            len = vcolumns.size();
        }

        if (token.equals("VALUES")) {
            tTokenizer.getThis("(");

            Object row[] = new Object[t.getColumnCount()];
            boolean check[] = (vcolumns == null)
                ? null
                : new boolean[row.length];
            boolean enclosed = false;
            int i = 0;

            for (; i < len; i++) {
                int colindex;

                if (vcolumns == null) {
                    colindex = i;
                }
                else {
//                    colindex = ServiceClass.getColumnNr( t, (String)vcolumns.get( i ) );
//                    check[colindex] = true;
                }

//                ColumnType column = t.getColumns().getItem( colindex );

//                row[colindex] = getValue( column.getType() );

                String fieldValue = tTokenizer.getString();

                token = tTokenizer.getString();
                tTokenizer.back();

                if (!fieldValue.equals("?") && !token.equals(",") && !token.equals(")")) {
                    int countBrasket = 0;
                    int countLoop = 30;
                    while (--countLoop > 0) {
                        token = tTokenizer.getString();

                        if (token.equals("("))
                            countBrasket++;

                        else if (token.equals(")")) {
                            countBrasket--;
                            if (countBrasket == 0)
                                break;
                        }
                    }
                }

                token = tTokenizer.getString();

                if (token.equals(",")) {
                    continue;
                }

                if (token.equals(")")) {
                    enclosed = true;
                    break;
                }


                throw new Exception("UNEXPECTED_TOKEN " + token);
            }

            if (!enclosed || i != len - 1) {
                throw new Exception("COLUMN_COUNT_DOES_NOT_MATCH");
            }

            count = 1;
        }
        else {
            boolean isBrasket = false;
            if (token.equals("(")) {
                isBrasket = true;
                token = tTokenizer.getString();
            }

            if (token.equals("SELECT")) {
                SelectType selectResult = parseSelect();
                insertTemp.setSelect(selectResult);
            }
            else {
                throw new Exception("UNEXPECTED_TOKEN " + token);
            }

            if (isBrasket)
                token = tTokenizer.getString();
        }
        return insertTemp;
    }

    /**
     * Pad or truncate a string to len size
     *
     * @param s   the string to pad to truncate
     * @param len the len to make the string
     * @param pad pad the string
     * @return the string of size len
     */
    static String padOrTrunc(String s, int len, boolean pad) {

        if (s.length() >= len) {
            return s.substring(0, len);
        }

        StringBuilder b = new StringBuilder(len);

        b.append(s);

        if (pad) {
            for (int i = s.length(); i < len; i++) {
                b.append(' ');
            }
        }

        return b.toString();
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    SelectType parseSelect()
        throws Exception {

        SelectType selectResult = new SelectType();

// fredt@users 20011010 - patch 471710 by fredt - LIMIT rewritten
// SELECT LIMIT n m DISTINCT ... queries and error message
// "SELECT LIMIT n m ..." creates the result set for the SELECT statement then
// discards the first n rows and returns m rows of the remaining result set
// "SELECT LIMIT 0 m" is equivalent to "SELECT TOP m" or "SELECT FIRST m"
// in other RDBMS's
// "SELECT LIMIT n 0" discards the first n rows and returns the remaining rows
// fredt@users 20020225 - patch 456679 by hiep256 - TOP keyword
        String token = tTokenizer.getString();

        if (token.equals("LIMIT")) {
            String limStart = tTokenizer.getString();
            String limEnd = tTokenizer.getString();

            try {
                selectResult.setLimitStart(Integer.parseInt(limStart));
                selectResult.setLimitCount(Integer.parseInt(limEnd));

                LimitType limit = new LimitType();

                limit.setStart(Integer.parseInt(limStart));
                limit.setCount(Integer.parseInt(limEnd));
            }
            catch (NumberFormatException ex) {

                String errorString = "WRONG_DATA_TYPE LIMIT n m";
                cat.error(errorString);
                throw new IllegalArgumentException(errorString);
            }

            token = tTokenizer.getString();
        }
        else if (token.equals("TOP")) {
            String limEnd = tTokenizer.getString();

            try {
                selectResult.setLimitStart(0);
                selectResult.setLimitCount(Integer.parseInt(limEnd));

                LimitType limit = new LimitType();

                limit.setStart(0);
                limit.setCount(Integer.parseInt(limEnd));
            }
            catch (NumberFormatException ex) {
                String errorString = "WRONG_DATA_TYPE TOP m";
                cat.error(errorString);
                throw new IllegalArgumentException(errorString);
            }

            token = tTokenizer.getString();
        }

        if (token.equals("DISTINCT")) {
            selectResult.setIsDistinctSelect(true);
        }
        else {
            tTokenizer.back();
        }

        // parse column list
        ArrayList<ExpressionType> vcolumn = new ArrayList<ExpressionType>();

        do {
            ExpressionType e = parseExpression();

            token = tTokenizer.getString();

            if (token.equals("AS")) {
                e.setAlias(tTokenizer.getName());

                token = tTokenizer.getString();
            }
            else if (tTokenizer.wasName()) {
                e.setAlias(token);

                token = tTokenizer.getString();
            }

            vcolumn.add(e);
        }
        while (token.equals(","));

        if (token.equals("INTO")) {

            token = tTokenizer.getString();
            selectResult.setIntoTableName(ServiceClass.getInstance(token, tTokenizer.wasQuotedIdentifier()));
            token = tTokenizer.getString();
        }

        if (!token.equals("FROM")) {
            throw new Exception("UNEXPECTED_TOKEN " + token);
        }

        ExpressionType condition = null;

        // parse table list
        ArrayList<TableFilterType> vfilter = new ArrayList<TableFilterType>();

        vfilter.add(parseTableFilter(false));

        while (true) {
            token = tTokenizer.getString();

            if (token.equals("LEFT")) {
                token = tTokenizer.getString();

                if (token.equals("OUTER")) {
                    token = tTokenizer.getString();
                }

                if (!token.equals("JOIN"))
                    throw new Exception("UNEXPECTED_TOKEN " + token);

                vfilter.add(parseTableFilter(true));
                tTokenizer.getThis("ON");

// thertz@users 20020320 - patch 473613 - outer join condition bug
// we now call parseJoinCondition() because a limitation of HSQLDB results
// in incorrect results for OUTER JOINS that have anything other than
// tableA.colA=tableB.colB type expressions
                //condition = addCondition(condition, parseExpression());
                condition = addCondition(condition,
                    parseOuterJoinCondition());
            }
            else if (token.equals("INNER")) {
                tTokenizer.getThis("JOIN");
                vfilter.add(parseTableFilter(false));
                tTokenizer.getThis("ON");

                condition = addCondition(condition, parseExpression());
            }
            else if (token.equals(",")) {
                vfilter.add(parseTableFilter(false));
            }
            else {
                break;
            }
        }

        tTokenizer.back();

        int len = vfilter.size();
        TableFilterType filter[] = new TableFilterType[len];

        vfilter.toArray(filter);

        selectResult.setTableFilter(filter);

        // expand [table.]* columns
        len = vcolumn.size();

        for (int i = 0; i < len; i++) {
            ExpressionType e = vcolumn.get(i);

            if (e.getType() == ExpressionService.ASTERIX) {
                int current = i;
                TableType table = null;
                String n = ExpressionService.getTableName(e);

                for (TableFilterType f : filter) {
                    ExpressionService.resolve(e, f);

                    if (n != null && !n.equals(f.getAlias())) {
                        continue;
                    }

                    table = f.getTable();

                    int col = table.getVisibleColumns();

                    for (int c = 0; c < col; c++) {
                        ExpressionType ins = ExpressionService.getInstance(f.getAlias(), (table.getColumns().getItem(c)).getColumnName().getOriginName(),
                            (table.getColumns().getItem(c)).getColumnName().getIsNameQuoted());

                        vcolumn.add(current++, ins);

                        // now there is one element more to parse
                        len++;
                    }
                }

//                throw new Exception( "TABLE_NOT_FOUND "+ n );

                // minus the asterix element
                len--;

                vcolumn.remove(current);
            }
            else if (e.getType() == ExpressionService.COLUMN) {
                if (ExpressionService.getTableName(e) == null) {
                    for (TableFilterType aFilter : filter) {
                        ExpressionService.resolve(e, aFilter);
                    }
                }
            }
        }

        selectResult.setResultLength(len);

        // where
        token = tTokenizer.getString();

        if (token.equals("WHERE")) {
            condition = addCondition(condition, parseExpression());
            token = tTokenizer.getString();
        }

        if (condition == null)
            selectResult.setExpCondition(new ExpressionType());
        else
            selectResult.setExpCondition(condition);

// fredt@users 20020215 - patch 1.7.0 by fredt
// to support GROUP BY with more than one column
        if (token.equals("GROUP")) {
            tTokenizer.getThis("BY");

            len = 0;

            do {
                ExpressionType e = parseExpression();

                // tony_lai@users having support:
                // "group by" does not allow refering to other columns alias.
                //e = doOrderGroup(e, vcolumn);
                vcolumn.add(e);

                token = tTokenizer.getString();

                len++;
            }
            while (token.equals(","));

            selectResult.setGroupLength(len);
        }

        if (token.equals("START")) {
            tTokenizer.getThis("WITH");

            String fieldName = tTokenizer.getString();
            tTokenizer.getThis("=");
            String startValue = tTokenizer.getString();
//            vcolumn.add( selectResult.getExpHavingCondition() );
            token = tTokenizer.getString();
        }

//        "CONNECT BY PRIOR id=id_main " +

        if (token.equals("CONNECT")) {
            tTokenizer.getThis("BY");
            tTokenizer.getThis("PRIOR");

            String fieldNameLeft = tTokenizer.getString();
            tTokenizer.getThis("=");
            String fieldNameRight = tTokenizer.getString();
//            vcolumn.add( selectResult.getExpHavingCondition() );
            token = tTokenizer.getString();
        }

        // tony_lai@users - having support
        if (token.equals("HAVING")) {
            selectResult.setHavingIndex(vcolumn.size());
            selectResult.setExpHavingCondition(parseExpression());
            token = tTokenizer.getString();

            vcolumn.add(selectResult.getExpHavingCondition());
        }

        if (token.equals("ORDER")) {
            tTokenizer.getThis("BY");

            len = 0;

            do {
                ExpressionType e = parseExpression();

                e = checkOrderByColumns(e, vcolumn);
                token = tTokenizer.getString();

                if (token.equals("DESC")) {
                    e.setIsDescending(true);

                    token = tTokenizer.getString();
                }
                else if (token.equals("ASC")) {
                    token = tTokenizer.getString();
                }

                vcolumn.add(e);

                len++;
            }
            while (token.equals(","));

            selectResult.setOrderLength(len);
        }

        len = vcolumn.size();
        selectResult.setExpColumn(new ExpressionType[len]);

        vcolumn.toArray(selectResult.getExpColumn());

        if (token.equals("UNION")) {
            token = tTokenizer.getString();

            if (token.equals("ALL")) {
                selectResult.setUnionType(UnionTypeType.UNIONALL);
            }
            else {
                selectResult.setUnionType(UnionTypeType.UNION);

                tTokenizer.back();
            }

            tTokenizer.getThis("SELECT");

            selectResult.setUnion(parseSelect());
        }
        else if (token.equals("INTERSECT")) {
            tTokenizer.getThis("SELECT");

            selectResult.setUnionType(UnionTypeType.INTERSECT);
            selectResult.setUnion(parseSelect());
        }
        else if (token.equals("EXCEPT") || token.equals("MINUS")) {
            tTokenizer.getThis("SELECT");

            selectResult.setUnionType(UnionTypeType.MINUS);
            selectResult.setUnion(parseSelect());
        }
        else {
            tTokenizer.back();
        }

        return selectResult;
    }

    /**
     * Checks Order By columns, and substitutes order by columns that is
     * refering to select columns by alias or column index.
     *
     * @param e       Description of the Parameter
     * @param vcolumn Description of the Parameter
     * @return Description of the Return Value
     * @throws Exception Description of the Exception
     */
    private ExpressionType checkOrderByColumns(ExpressionType e,
        ArrayList vcolumn)
        throws Exception {

        if (e.getType() == ExpressionService.VALUE) {

            // order by 1,2,3
            if (e.getDataType() == Types.INTEGER) {
                int i = Integer.parseInt(e.getObjectData());

                e = (ExpressionType) vcolumn.get(i - 1);
            }
        }
        else if (e.getType() == ExpressionService.COLUMN
            && ExpressionService.getTableName(e) == null) {

            // this could be an alias column
            String s = ExpressionService.getColumnName(e);

            for (int i = 0, size = vcolumn.size(); i < size; i++) {
                ExpressionType ec = (ExpressionType) vcolumn.get(i);

                // We can only substitute alias defined in the select clause,
                // since there may be more that one result column with the
                // same column name.  For example:
                //   "select 500-column1, column1 from table 1 order by column2"
                if (s.equals(ec.getAlias())) {
                    e = ec;

                    break;
                }
            }
        }

        return e;
    }

    /**
     * Method declaration
     *
     * @param outerjoin
     * @return
     * @throws Exception
     */
    private TableFilterType parseTableFilter(boolean outerjoin)
        throws Exception {

        String token = tTokenizer.getString();
        TableType t = null;

        if (token.equals("(")) {
            tTokenizer.getThis("SELECT");

            SelectType s = parseSelect();
//            t = ServiceClass.getInstance( ServiceClass.getInstance( "SYSTEM_SUBQUERY", false ) );
            t = new TableType();
            t.setSubQuery(ExpressionService.getInstance(s));

            tTokenizer.getThis(")");
//            t.addColumns( r );
        }
        else {
            t = ServiceClass.getInstance(ServiceClass.getInstance(token, false));
        }

        String sAlias = null;

        token = tTokenizer.getString();

        if (token.equals("AS")) {
            sAlias = tTokenizer.getName();
        }
        else if (tTokenizer.wasName()) {
            sAlias = token;
        }
        else {
            tTokenizer.back();
        }

        return TableService.getInstance(t, sAlias, outerjoin);
    }

    /**
     * Method declaration
     *
     * @param e1
     * @param e2
     * @return ExpressionType
     */
    private ExpressionType addCondition(ExpressionType e1, ExpressionType e2) {

        if (e1 == null) {
            return e2;
        }
        else if (e2 == null) {
            return e1;
        }
        else {
            return ExpressionService.getInstance(ExpressionService.AND, e1, e2);
        }
    }

    /**
     * parses the expression that can be used behind a
     * [..] JOIN table ON (exp).
     * This expression should always be in the form "tab.col=tab2.col"
     * with optional brackets (to support automated query tools).<br>
     * this method is used from the parseSelect method
     *
     * @return the expression
     * @throws Exception if the syntax was not correct
     */
    private ExpressionType parseOuterJoinCondition()
        throws Exception {

        boolean parens = false;

        read();

        if (iToken == ExpressionService.OPEN) {
            parens = true;

            read();
        }

        if (iToken != ExpressionService.COLUMN)
            throw new Exception("OUTER_JOIN_CONDITION");

        ExpressionType left = ExpressionService.getInstance(sTable, sToken);

        read();
        if (iToken != ExpressionService.EQUAL)
            throw new Exception("OUTER_JOIN_CONDITION");

        read();
        if (iToken != ExpressionService.COLUMN)
            throw new Exception("OUTER_JOIN_CONDITION");

        ExpressionType right = ExpressionService.getInstance(sTable, sToken);

        if (parens) {
            read();
            if (iToken != ExpressionService.CLOSE)
                throw new Exception("OUTER_JOIN_CONDITION");
        }

        return ExpressionService.getInstance(ExpressionService.EQUAL, left, right);
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType parseExpression()
        throws Exception {

        read();

        ExpressionType r = readOr();

        tTokenizer.back();

        return r;
    }

    private ExpressionType readAggregate()
        throws Exception {

        boolean distinct = false;
        int type = iToken;

        read();

        if (tTokenizer.getString().equals("DISTINCT")) {
            distinct = true;
        }
        else {
            tTokenizer.back();
        }

        readThis(ExpressionService.OPEN);

        ExpressionType s = readOr();

        readThis(ExpressionService.CLOSE);

        ExpressionType aggregateExp = ExpressionService.getInstance(type, s, null);

        ExpressionService.setDistinctAggregate(aggregateExp, distinct);

        return aggregateExp;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readOr()
        throws Exception {

        ExpressionType r = readAnd();

        while (iToken == ExpressionService.OR) {
            int type = iToken;
            ExpressionType a = r;

            read();

            r = ExpressionService.getInstance(type, a, readAnd());
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readAnd()
        throws Exception {

        ExpressionType r = readCondition();

        while (iToken == ExpressionService.AND) {
            int type = iToken;
            ExpressionType a = r;

            read();

            r = ExpressionService.getInstance(type, a, readCondition());
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readCondition()
        throws Exception {

        switch (iToken) {

            case ExpressionService.NOT:
                {
                    int type = iToken;

                    read();

                    return ExpressionService.getInstance(type, readCondition(), null);
                }
            case ExpressionService.EXISTS:
                {
                    int type = iToken;

                    read();
                    readThis(ExpressionService.OPEN);
                    if (iToken != ExpressionService.SELECT)
                        throw new Exception("UNEXPECTED_TOKEN");

                    ExpressionType s = ExpressionService.getInstance(parseSelect());

                    read();
                    readThis(ExpressionService.CLOSE);

                    return ExpressionService.getInstance(type, s, null);
                }
            default :
                {
                    ExpressionType a = readConcat();
                    boolean not = false;

                    if (iToken == ExpressionService.NOT) {
                        not = true;

                        read();
                    }

                    switch (iToken) {

                        case ExpressionService.LIKE:
                            {
                                read();

                                ExpressionType b = readConcat();
                                String escape = "";

                                if (sToken.equals("ESCAPE")) {
                                    read();

                                    ExpressionType c = readTerm();

                                    if (c.getType() != ExpressionService.VALUE)
                                        throw new Exception("INVALID_ESCAPE");

/*
                                    String s = (String)c.getValue( Types.VARCHAR );

                                    if ( s==null || s.length()<1 )
                                    {
                                        throw new Exception( "INVALID_ESCAPE "+ s );
                                    }

                                    escape = s.charAt( 0 );
*/
                                }

                                a = ExpressionService.getInstance(ExpressionService.LIKE, a, b);

                                a.setCharLikeEscape(escape);

                                break;
                            }
                        case ExpressionService.BETWEEN:
                            {
                                read();

                                ExpressionType l = ExpressionService.getInstance(ExpressionService.BIGGER_EQUAL,
                                    a, readConcat());

                                readThis(ExpressionService.AND);

                                ExpressionType h =
                                    ExpressionService.getInstance(ExpressionService.SMALLER_EQUAL, a,
                                        readConcat());

                                a = ExpressionService.getInstance(ExpressionService.AND, l, h);

                                break;
                            }
                        case ExpressionService.IN:
                            {
                                int type = iToken;

                                read();
                                readThis(ExpressionService.OPEN);

                                ExpressionType b = null;

                                if (iToken == ExpressionService.SELECT) {
                                    b = ExpressionService.getInstance(parseSelect());

                                    read();
                                }
                                else {
                                    tTokenizer.back();

                                    ArrayList<ExpressionType> v = new ArrayList<ExpressionType>();

                                    while (true) {
                                        v.add(getValue(Types.VARCHAR));
                                        read();

                                        if (iToken != ExpressionService.COMMA) {
                                            break;
                                        }
                                    }

                                    b = ExpressionService.getInstance(v);
                                }

                                readThis(ExpressionService.CLOSE);

                                a = ExpressionService.getInstance(type, a, b);

                                break;
                            }
                        default :
                            {
                                if (not)
                                    throw new Exception("UNEXPECTED_TOKEN");

                                if (ExpressionService.isCompare(iToken)) {
                                    int type = iToken;

                                    read();

                                    return ExpressionService.getInstance(type, a, readConcat());
                                }

                                return a;
                            }
                    }

                    if (not) {
                        a = ExpressionService.getInstance(ExpressionService.NOT, a, null);
                    }

                    return a;
                }
        }
    }

    private ExpressionType getValue(int type) throws Exception {

        ExpressionType r = parseExpression();

//        r.resolve(null);

//        return r.getValue(type);
        return r;
    }

    /**
     * Method declaration
     *
     * @param type
     * @throws Exception
     */
    private void readThis(int type)
        throws Exception {
        if (iToken != type)
            throw new Exception("UNEXPECTED_TOKEN");
        read();
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readConcat()
        throws Exception {

        ExpressionType r = readSum();

        while (iToken == ExpressionService.STRINGCONCAT) {
            int type = ExpressionService.CONCAT;
            ExpressionType a = r;

            read();

            r = ExpressionService.getInstance(type, a, readSum());
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readSum()
        throws Exception {

        ExpressionType r = readFactor();

        while (true) {
            int type;

            if (iToken == ExpressionService.PLUS) {
                type = ExpressionService.ADD;
            }
            else if (iToken == ExpressionService.NEGATE) {
                type = ExpressionService.SUBTRACT;
            }
            else {
                break;
            }

            ExpressionType a = r;

            read();

            r = ExpressionService.getInstance(type, a, readFactor());
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readFactor()
        throws Exception {

        ExpressionType r = readTerm();

        while (iToken == ExpressionService.MULTIPLY || iToken == ExpressionService.DIVIDE) {
            int type = iToken;
            ExpressionType a = r;

            read();

            r = ExpressionService.getInstance(type, a, readTerm());
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @return
     * @throws Exception
     */
    private ExpressionType readTerm()
        throws Exception {

        ExpressionType r = null;

        switch (iToken) {

            case ExpressionService.COLUMN:
                {
                    r = ExpressionService.getInstance(sTable, sToken);

                    read();

                    if (iToken == ExpressionService.OPEN) {
//                        Function f = new Function(dDatabase.getAlias(name),
//                            cSession);
//                        int len = f.getArgCount();
//                        int i   = 0;

                        read();

// Todo work around recursive call of functions: to_number(to_char(month,'mm'))
                        if (iToken != ExpressionService.CLOSE) {
                            int countBrasket = 1;
                            while (true) {
//                                f.setArgument(i++, readOr());
                                read();

                                if (iToken == ExpressionService.OPEN)
                                    countBrasket++;

                                else if (iToken == ExpressionService.CLOSE) {
                                    countBrasket--;
                                    if (countBrasket == 0)
                                        break;
                                }

//                                if ( iToken != ExpressionService.COMMA )
//                                {
//                                    break;
//                                }

//                                read();
                            }
                        }

                        readThis(ExpressionService.CLOSE);
                    }

                    break;
                }
            case ExpressionService.NEGATE:
                {
                    int type = iToken;

                    read();

                    r = ExpressionService.getInstance(type, readTerm(), null);

                    break;
                }
            case ExpressionService.PLUS:
                {
                    read();

                    r = readTerm();

                    break;
                }
            case ExpressionService.OPEN:
                {
                    read();

                    r = readOr();

                    if (iToken != ExpressionService.CLOSE) {
                        throw new Exception("UNEXPECTED_TOKEN " + sToken);
                    }

                    read();

                    break;
                }
            case ExpressionService.VALUE:
                {
                    r = ExpressionService.getInstance(iType, oData);

                    read();

                    break;
                }
            case ExpressionService.SELECT:
                {
                    r = ExpressionService.getInstance(parseSelect());

                    read();

                    break;
                }
            case ExpressionService.MULTIPLY:
            case ExpressionService.END:
                {
                    r = ExpressionService.getInstance(sTable, null);

                    read();

                    break;
                }
            case ExpressionService.IFNULL:
            case ExpressionService.CONCAT:
                {
                    int type = iToken;

                    read();
                    readThis(ExpressionService.OPEN);

                    r = readOr();

                    readThis(ExpressionService.COMMA);

                    r = ExpressionService.getInstance(type, r, readOr());

                    readThis(ExpressionService.CLOSE);

                    break;
                }
            case ExpressionService.CASEWHEN:
                {
                    int type = iToken;

                    read();
                    readThis(ExpressionService.OPEN);

                    r = readOr();

                    readThis(ExpressionService.COMMA);

                    ExpressionType thenelse = readOr();

                    readThis(ExpressionService.COMMA);

                    // thenelse part is never evaluated; only init
                    thenelse = ExpressionService.getInstance(type, thenelse, readOr());
                    r = ExpressionService.getInstance(type, r, thenelse);

                    readThis(ExpressionService.CLOSE);

                    break;
                }
            case ExpressionService.CONVERT:
                {
                    int type = iToken;

                    read();
                    readThis(ExpressionService.OPEN);

                    r = readOr();

                    readThis(ExpressionService.COMMA);

                    int t = ServiceClass.getTypeNr(sToken);

                    r = ExpressionService.getInstance(type, r, null);

                    r.setDataType(t);
                    read();
                    readThis(ExpressionService.CLOSE);

                    break;
                }
            case ExpressionService.CAST:
                {
                    read();
                    readThis(ExpressionService.OPEN);

                    r = readOr();

                    if (!sToken.equals("AS"))
                        throw new Exception("UNEXPECTED_TOKEN " + sToken);

                    read();

                    int t = ServiceClass.getTypeNr(sToken);

                    r = ExpressionService.getInstance(ExpressionService.CONVERT, r, null);

                    r.setDataType(t);
                    read();
                    readThis(ExpressionService.CLOSE);

                    break;
                }
            default :
                {
                    if (ExpressionService.isAggregate(iToken)) {
                        r = readAggregate();
                    }
                    else {
                        throw new Exception("UNEXPECTED_TOKEN " + sToken + ", iToken " + iToken);
                    }

                    break;
                }
        }

        return r;
    }

    /**
     * Method declaration
     *
     * @throws Exception
     */

// fredt@users 20020130 - patch 497872 by Nitin Chauhan
// reordering for speed
    private void read()
        throws Exception {

        sToken = tTokenizer.getString();

        if (tTokenizer.wasValue()) {
            iToken = ExpressionService.VALUE;
            oData = tTokenizer.getAsValue();
            iType = tTokenizer.getType();
        }
        else if (tTokenizer.wasName()) {
            iToken = ExpressionService.COLUMN;
            sTable = null;
        }
        else if (tTokenizer.wasLongName()) {
            sTable = tTokenizer.getLongNameFirst();
            sToken = tTokenizer.getLongNameLast();

            if (sToken.equals("*")) {
                iToken = ExpressionService.MULTIPLY;
            }
            else {
                iToken = ExpressionService.COLUMN;
            }
        }
        else if (sToken.length() == 0) {
            iToken = ExpressionService.END;
        }
        else {
            Integer n = (Integer) tokenTable.get(sToken);

            if (n != null) {
                iToken = n.intValue();
            }
            else {
                iToken = ExpressionService.END;
            }

            switch (iToken) {

                case ExpressionService.COMMA:
                case ExpressionService.EQUAL:
                case ExpressionService.NOT_EQUAL:
                case ExpressionService.SMALLER:
                case ExpressionService.BIGGER:
                case ExpressionService.SMALLER_EQUAL:
                case ExpressionService.BIGGER_EQUAL:
                case ExpressionService.AND:
                case ExpressionService.OR:
                case ExpressionService.NOT:
                case ExpressionService.IN:
                case ExpressionService.EXISTS:
                case ExpressionService.BETWEEN:
                case ExpressionService.PLUS:
                case ExpressionService.NEGATE:
                case ExpressionService.DIVIDE:
                case ExpressionService.STRINGCONCAT:
                case ExpressionService.OPEN:
                case ExpressionService.CLOSE:
                case ExpressionService.SELECT:
                case ExpressionService.LIKE:
                case ExpressionService.COUNT:
                case ExpressionService.SUM:
                case ExpressionService.MIN:
                case ExpressionService.MAX:
                case ExpressionService.AVG:
                case ExpressionService.IFNULL:
                case ExpressionService.CONVERT:
                case ExpressionService.CAST:
                case ExpressionService.CASEWHEN:
                case ExpressionService.CONCAT:
                case ExpressionService.END:
                    break;            // nothing else required, iToken initialized properly

                case ExpressionService.MULTIPLY:
                    sTable = null;    // in case of ASTERIX
                    break;

                case ExpressionService.IS:
                    sToken = tTokenizer.getString();

                    if (sToken.equals("NOT")) {
                        iToken = ExpressionService.NOT_EQUAL;
                    }
                    else {
                        iToken = ExpressionService.EQUAL;

                        tTokenizer.back();
                    }
                    break;

                default :
                    iToken = ExpressionService.END;
            }
        }
    }

    private static java.util.Map<String, Integer> tokenTable =
        new java.util.HashMap<String, Integer>(37);

    static {
        tokenTable.put(",", ExpressionService.COMMA);
        tokenTable.put("=", ExpressionService.EQUAL);
        tokenTable.put("!=", ExpressionService.NOT_EQUAL);
        tokenTable.put("<>", ExpressionService.NOT_EQUAL);
        tokenTable.put("<", ExpressionService.SMALLER);
        tokenTable.put(">", ExpressionService.BIGGER);
        tokenTable.put("<=", ExpressionService.SMALLER_EQUAL);
        tokenTable.put(">=", ExpressionService.BIGGER_EQUAL);
        tokenTable.put("AND", ExpressionService.AND);
        tokenTable.put("NOT", ExpressionService.NOT);
        tokenTable.put("OR", ExpressionService.OR);
        tokenTable.put("IN", ExpressionService.IN);
        tokenTable.put("EXISTS", ExpressionService.EXISTS);
        tokenTable.put("BETWEEN", ExpressionService.BETWEEN);
        tokenTable.put("+", ExpressionService.PLUS);
        tokenTable.put("-", ExpressionService.NEGATE);
        tokenTable.put("*", ExpressionService.MULTIPLY);
        tokenTable.put("/", ExpressionService.DIVIDE);
        tokenTable.put("||", ExpressionService.STRINGCONCAT);
        tokenTable.put("(", ExpressionService.OPEN);
        tokenTable.put(")", ExpressionService.CLOSE);
        tokenTable.put("SELECT", ExpressionService.SELECT);
        tokenTable.put("LIKE", ExpressionService.LIKE);
        tokenTable.put("COUNT", ExpressionService.COUNT);
        tokenTable.put("SUM", ExpressionService.SUM);
        tokenTable.put("MIN", ExpressionService.MIN);
        tokenTable.put("MAX", ExpressionService.MAX);
        tokenTable.put("AVG", ExpressionService.AVG);
        tokenTable.put("IFNULL", ExpressionService.IFNULL);
        tokenTable.put("CONVERT", ExpressionService.CONVERT);
        tokenTable.put("CAST", ExpressionService.CAST);
        tokenTable.put("CASEWHEN", ExpressionService.CASEWHEN);
        tokenTable.put("CONCATE", ExpressionService.CONCAT);
        tokenTable.put("IS", ExpressionService.IS);
    }



//    private String sName;
//    private UserManager aAccess;
//    private HsqlArrayList table;
//    boolean bReadOnly;
//    private boolean bShutdown;
//    private HashMap hAlias;
//    private boolean bIgnoreCase;
//    private boolean bReferentialIntegrity;
//    private HsqlArrayList cSession;
//    private Session sysSession;

    //for execute()
    public static final int CALL = 1;
    public static final int CHECKPOINT = 2;
    public static final int COMMIT = 3;
    public static final int CONNECT = 4;
    public static final int CREATE = 5;
    public static final int DELETE = 6;
    public static final int DISCONNECT = 7;
    public static final int DROP = 8;
    public static final int GRANT = 9;
    public static final int INSERT = 10;
    public static final int REVOKE = 11;
    public static final int ROLLBACK = 12;
    public static final int SAVEPOINT = 13;
    public static final int SCRIPT = 14;
    public static final int SELECT = 15;
    public static final int SET = 16;
    public static final int SHUTDOWN = 17;
    public static final int UPDATE = 18;
    public static final int SEMICOLON = 19;
    public static final int ALTER = 20;
    public static final int ADD = 24;
    public static final int ALIAS = 35;
    public static final int AUTOCOMMIT = 43;
    public static final int CACHED = 31;
    public static final int COLUMN = 27;
    public static final int CONSTRAINT = 25;
    public static final int FOREIGN = 26;
    public static final int IGNORECASE = 41;
    public static final int INDEX = 22;
    public static final int LOGSIZE = 39;
    public static final int LOGTYPE = 40;
    public static final int MAXROWS = 42;
    public static final int MEMORY = 30;
    public static final int PASSWORD = 37;
    public static final int PRIMARY = 36;
    public static final int READONLY = 38;
    public static final int REFERENTIAL_INTEGRITY = 46;
    public static final int RENAME = 23;
    public static final int SOURCE = 44;

    //for process*()
    public static final int TABLE = 21;
    public static final int TEXT = 29;
    public static final int TRIGGER = 33;
    public static final int UNIQUE = 28;
    public static final int USER = 34;
    public static final int VIEW = 32;
    public static final int WRITE_DELAY = 45;

    public static final int START = 1001;
//    public static final int CONNECT = 1002;

    public static final Map<String, Integer> hCommands = new HashMap<String, Integer>(67, 1);

    public static Parser getInstance(String statement)
        throws Exception {
        if (statement == null && statement.length() == 0)
            return null;

        return new Parser(statement);
    }

    static {
        hCommands.put("ALTER", ALTER);
        hCommands.put("CALL", CALL);
        hCommands.put("CHECKPOINT", CHECKPOINT);
        hCommands.put("COMMIT", COMMIT);
        hCommands.put("CONNECT", CONNECT);
        hCommands.put("CREATE", CREATE);
        hCommands.put("DELETE", DELETE);
        hCommands.put("DISCONNECT", DISCONNECT);
        hCommands.put("DROP", DROP);
        hCommands.put("GRANT", GRANT);
        hCommands.put("INSERT", INSERT);
        hCommands.put("REVOKE", REVOKE);
        hCommands.put("ROLLBACK", ROLLBACK);
        hCommands.put("SAVEPOINT", SAVEPOINT);
        hCommands.put("SCRIPT", SCRIPT);
        hCommands.put("SELECT", SELECT);
        hCommands.put("SET", SET);
        hCommands.put("SHUTDOWN", SHUTDOWN);
        hCommands.put("UPDATE", UPDATE);
        hCommands.put(";", SEMICOLON);

        //
        hCommands.put("TABLE", TABLE);
        hCommands.put("INDEX", INDEX);
        hCommands.put("RENAME", RENAME);
        hCommands.put("ADD", ADD);
        hCommands.put("CONSTRAINT", CONSTRAINT);
        hCommands.put("FOREIGN", FOREIGN);
        hCommands.put("COLUMN", COLUMN);
        hCommands.put("UNIQUE", UNIQUE);
        hCommands.put("TEXT", TEXT);
        hCommands.put("MEMORY", MEMORY);
        hCommands.put("CACHED", CACHED);
        hCommands.put("VIEW", VIEW);
        hCommands.put("TRIGGER", TRIGGER);
        hCommands.put("USER", USER);
        hCommands.put("ALIAS", ALIAS);
        hCommands.put("PRIMARY", PRIMARY);
        hCommands.put("PASSWORD", PASSWORD);
        hCommands.put("READONLY", READONLY);
        hCommands.put("LOGSIZE", LOGSIZE);
        hCommands.put("LOGTYPE", LOGTYPE);
        hCommands.put("IGNORECASE", IGNORECASE);
        hCommands.put("MAXROWS", MAXROWS);
        hCommands.put("AUTOCOMMIT", AUTOCOMMIT);
        hCommands.put("SOURCE", SOURCE);
        hCommands.put("WRITE_DELAY", WRITE_DELAY);
        hCommands.put("REFERENTIAL_INTEGRITY", REFERENTIAL_INTEGRITY);
        hCommands.put("START", START);
    }
}
