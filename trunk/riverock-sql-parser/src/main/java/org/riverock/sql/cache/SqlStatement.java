/*
 * org.riverock.sql - Classes for tracking database changes
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.sql.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.riverock.sql.parser.Parser;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: May 10, 2003
 * Time: 6:16:50 PM
 * <p/>
 * $Id$
 */
public class SqlStatement {
    private static Logger log = Logger.getLogger(SqlStatement.class);

    private static Map<String, Object> sqlParserHash = new HashMap<String, Object>(100);
    public static Map<String, Object> classHash = new HashMap<String, Object>();
    public static Map<String, Object> classRelateHash = new HashMap<String, Object>();

    public synchronized static Parser parseSql(String sql) throws Exception {
        if (log.isDebugEnabled())
            log.debug("sql\n" + sql);

        if (sql == null || sql.length() == 0)
            return null;

        try {
            Object obj = sqlParserHash.get(sql);

            if (obj == null) {
                String clone = new String(sql.toCharArray());
                Parser parserTemp = (Parser) sqlParserHash.get(clone);

                if (log.isDebugEnabled())
                    log.debug("sql\n" + clone + "\nresult search parsed result in hash" + parserTemp);

                if (parserTemp == null) {
                    if (log.isDebugEnabled())
                        log.debug("put new result of parsing");

                    parserTemp = Parser.getInstance(clone);
                    sqlParserHash.put(clone, parserTemp);
                }
                return parserTemp;
            } else if (obj instanceof Parser) {
                Parser tempParserObj = (Parser) obj;
                if (sql.equals(tempParserObj.sql))
                    return tempParserObj;

                String clone = new String(sql.toCharArray());
                Parser newParser = Parser.getInstance(clone);

                List<Parser> v = new ArrayList<Parser>(4);
                v.add(tempParserObj);
                v.add(newParser);

                sqlParserHash.put(clone, v);
                return tempParserObj;
            }
            else if (obj instanceof List) {
                List<Parser> arrayList = ((List<Parser>) obj);
                for (int i = 0; i < arrayList.size(); i++) {
                    Parser tempParser = (Parser) arrayList.get(i);
                    if (tempParser == null)
                        continue;

                    if (tempParser.sql.equals(sql))
                        return tempParser;
                }

                String clone = new String(sql.toCharArray());
                Parser newParser = Parser.getInstance(clone);
                arrayList.add(newParser);

                return newParser;
            }
            else {
                String errorString = "Object in hash is " + obj.getClass().getName();
                log.error(errorString);
                throw new Exception(errorString);
            }
        }
        catch (Exception e) {
            log.error("Error parse SQL " + sql, e);
            throw e;
        }
    }

    public synchronized static void registerRelateClass(Class classTarget, Class classMain) {
        if (classTarget == null || classMain == null)
            throw new IllegalArgumentException("Both classes in relation must be not null");

        if (classTarget == classMain)
            throw new IllegalArgumentException("Can not set relation to self");

        Object obj = classRelateHash.get(classMain.getName());
        if (obj == null) {
            classRelateHash.put(classMain.getName(), classTarget.getName());
        } else if (obj instanceof List) {
            ((List<String>) obj).add(classTarget.getName());
        } else {
            String name = (String) obj;
            List<String> v = new ArrayList<String>();
            v.add(name);
            v.add(classTarget.getName());
            classRelateHash.put(classMain.getName(), v);
        }
    }

    public synchronized static void registerSql(String sql_, Class class_) throws Exception {
        if (sql_ == null || class_ == null)
            return;

        try {
            Parser parser = parseSql(sql_);

            Object obj = classHash.get(class_.getName());
            if (obj == null) {
                classHash.put(class_.getName(), parser);
            } else if (obj instanceof List) {
                ((List<Parser>) obj).add(parser);
            } else {
                Parser tempParser = (Parser) obj;
                List<Parser> v = new ArrayList<Parser>();
                v.add(tempParser);
                v.add(parser);
                classHash.put(class_.getName(), v);
            }
        }
        catch (Exception e) {
            log.error("Error parser sql:\n" + sql_, e);
            throw e;
        }
    }
}
