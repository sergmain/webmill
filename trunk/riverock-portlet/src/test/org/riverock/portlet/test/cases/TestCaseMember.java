/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * User: Admin
 * Date: Dec 14, 2002
 * Time: 2:22:59 PM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.member.MemberFile;
import org.riverock.portlet.member.ModuleManager;
import org.riverock.portlet.member.TemplateMemberClassQuery;
import org.riverock.portlet.member.MemberServiceClass;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.portlet.schema.member.ClassQueryType;
import org.riverock.portlet.schema.member.ContentType;
import org.riverock.portlet.schema.member.FieldsType;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.schema.member.QueryAreaType;
import org.riverock.portlet.schema.member.RestrictType;
import org.riverock.portlet.schema.member.TableType;
import org.riverock.portlet.schema.member.TargetModuleType;
import org.riverock.portlet.schema.member.RelateClassType;
import org.riverock.portlet.schema.member.types.*;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.generic.tools.servlet.HttpServletRequestApplWrapper;
import org.riverock.sql.parser.Parser;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.webmill.portlet.wrapper.RenderRequestImpl;

import javax.portlet.PortletRequest;

public class TestCaseMember extends TestCase
{
    private static boolean isUseAssertion = true;


    public TestCaseMember(String testName)
    {
        super(testName);
    }

    private DbSchemaType schemaObj = null;
    private DbSchemaType getDbSchema()
        throws Exception
    {

        if (schemaObj==null)
        {
            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");

            System.out.println("Get schema of DB");
            schemaObj = DatabaseManager.getDbStructure(db_ );
        }
        return schemaObj;
    }


    // ��������� ������ ����� � QueryArea �� ������� � ���� ������
    public void testMemberFields()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");
//
//        System.out.println("Get schema of DB");
        DbSchemaType schema = getDbSchema();

//        System.out.println("Write schema to file");
//        XmlTools.writeToFile(schema, WebmillConfig.getWebmillDebugDir()+"-validate-field.xml");

        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
//                System.out.println("Module "+mod.getName());
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

//                if (!"currency.currency".equals(mod.getName()))
//                    continue;

//                System.out.println("Start check module "+mod.getName());
                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    for (int t = 0; t < qa.getTableCount(); t++)
                    {
                        TableType table = qa.getTable(t);
                        DbTableType dbTable = DatabaseManager.getTableFromStructure(schema, table.getTable());
                        if (dbTable != null)
                        {
                            for (int k = 0; k < qa.getFieldsCount(); k++)
                            {
                                FieldsType field = qa.getFields(k);
                                DbFieldType dbField = null;
                                for (int qq=0; qq<dbTable.getFieldsCount(); qq++)
                                {
                                    DbFieldType dbFieldType = dbTable.getFields(qq);
                                    if ( dbFieldType.getName().equals(field.getName()))
                                    {

//                                        System.out.println("field "+field.getName());
                                        if ((content.getAction().getType()==ContentTypeActionType.CHANGE_TYPE ||
                                            content.getAction().getType()==ContentTypeActionType.INSERT_TYPE
                                            ) &&
                                            new Integer(DatabaseMetaData.columnNoNulls).equals(dbFieldType.getNullable()))
                                        {

                                            if (isUseAssertion)
                                            {
                                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                                    "Checked table '"+table.getTable()+"'\n" +
                                                    "Sturctured table '"+dbTable.getName()+"'\n" +
                                                    "module '" + mod.getName() +
                                                    "', content action '" + content.getAction().toString() +
                                                    "' field '"+field.getName()+"' can not be null, need change IsNotNull to 'true'",
                                                    Boolean.TRUE.equals(field.getIsShow()) &&
                                                    Boolean.TRUE.equals(field.getIsEdit()) &&
                                                    Boolean.FALSE.equals(field.getIsNotNull())
                                                );

                                            }
                                            else
                                            {

                                                if (Boolean.TRUE.equals(field.getIsShow()) &&
                                                    Boolean.TRUE.equals(field.getIsEdit()) && Boolean.FALSE.equals(field.getIsNotNull()) )
                                                    System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                        "Checked table '"+table.getTable()+"'\n" +
                                                        "Sturctured table '"+dbTable.getName()+"'\n" +
                                                        "module '" + mod.getName() +
                                                        "', content action '" + content.getAction().toString() +
                                                        "' field '"+field.getName()+"' can not be null, need change IsNotNull to 'true'"

                                                    );
                                            }
                                        }

                                        switch(dbFieldType.getJavaType().intValue())
                                        {
                                            case Types.DECIMAL:
                                            case Types.NUMERIC:
                                            case Types.DOUBLE:
                                            case Types.INTEGER:
                                            case Types.REAL:
                                            case Types.TINYINT:
                                                if (isUseAssertion)
                                                {
                                                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                                        "Checked table '"+table.getTable()+"'\n" +
                                                        "Sturctured table '"+dbTable.getName()+"'\n" +
                                                        "module '" + mod.getName() +
                                                        "', content action '" + content.getAction().toString() +
                                                        "' field '"+field.getName()+"' has wrong type",
                                                        field.getDbType().getType()!=FieldsTypeDbTypeType.NUMBER_TYPE
                                                    );
                                                }
                                                else
                                                {
                                                    if (field.getDbType().getType()!=FieldsTypeDbTypeType.NUMBER_TYPE )
                                                        System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                            "Checked table '"+table.getTable()+"'\n" +
                                                            "Sturctured table '"+dbTable.getName()+"'\n" +
                                                            "module '" + mod.getName() +
                                                            "', content action '" + content.getAction().toString() +
                                                            "' field '"+field.getName()+"' has wrong type"
                                                        );
                                                }
                                                break;

                                            case Types.VARCHAR:
                                            case Types.CHAR:
                                                if (isUseAssertion)
                                                {
                                                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                                        "Checked table '"+table.getTable()+"'\n" +
                                                        "Sturctured table '"+dbTable.getName()+"'\n" +
                                                        "module '" + mod.getName() +
                                                        "', content action '" + content.getAction().toString() +
                                                        "' field '"+field.getName()+"' has wrong type",
                                                        field.getDbType().getType()!=FieldsTypeDbTypeType.VARCHAR_TYPE
                                                    );
                                                }
                                                else
                                                {
                                                    if (field.getDbType().getType()!=FieldsTypeDbTypeType.VARCHAR_TYPE )
                                                        System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                            "Checked table '"+table.getTable()+"'\n" +
                                                            "Sturctured table '"+dbTable.getName()+"'\n" +
                                                            "module '" + mod.getName() +
                                                            "', content action '" + content.getAction().toString() +
                                                            "' field '"+field.getName()+"' has wrong type"
                                                        );
                                                }
                                                break;

                                            case Types.DATE:
                                            case Types.TIME:
                                            case Types.TIMESTAMP:
                                                if (isUseAssertion)
                                                {
                                                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                                        "Checked table '"+table.getTable()+"'\n" +
                                                        "Sturctured table '"+dbTable.getName()+"'\n" +
                                                        "module '" + mod.getName() +
                                                        "', content action '" + content.getAction().toString() +
                                                        "' field '"+field.getName()+"' has wrong type",
                                                        field.getDbType().getType()!=FieldsTypeDbTypeType.DATE_TYPE
                                                    );
                                                }
                                                else
                                                {
                                                    if (field.getDbType().getType()!=FieldsTypeDbTypeType.DATE_TYPE )
                                                        System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                            "Checked table '"+table.getTable()+"'\n" +
                                                            "Sturctured table '"+dbTable.getName()+"'\n" +
                                                            "module '" + mod.getName() +
                                                            "', content action '" + content.getAction().toString() +
                                                            "' field '"+field.getName()+"' has wrong type"
                                                        );
                                                }
                                                break;
                                        }
                                        dbField = dbFieldType;
                                        break;
                                    }
                                }
//                                byte[] originByte = XmlTools.getXml( dbTable, null);
//                                MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"bytes-structure-table.xml", originByte);
//                                byte[] fieldByte = XmlTools.getXml( field, null);
//                                MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"bytes-structure-field.xml", fieldByte);

                                if (isUseAssertion)
                                {
                                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                        "Checked table '"+table.getTable()+"'\n" +
                                        "Sturctured table '"+dbTable.getName()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "' containt unknown field '"+field.getName()+"' ",
                                        dbField==null
                                    );
                                }
                                else
                                {
                                    if (dbField==null)
                                        System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                            "Checked table '"+table.getTable()+"'\n" +
                                            "Sturctured table '"+dbTable.getName()+"'\n" +
                                            "module '" + mod.getName() +
                                            "', content action '" + content.getAction().toString() +
                                            "' containt unknown field '"+field.getName()+"' "
                                        );
                                }
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "' containt field 'IS_CURRENT' with wrong type '"+field.getJspType().toString()+"' ",
                                    "IS_CURRENT".equalsIgnoreCase( field.getName()) &&
                                    field.getJspType().getType()!=FieldsTypeJspTypeType.YES_1_NO_N_TYPE
                                );
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "' containt field 'IS_CURRENT' with wrong type '"+field.getJspType().toString()+"' ",
                                    field.getDbType().getType()==FieldsTypeDbTypeType.NUMBER_TYPE &&
                                    field.getJspType().getType()==FieldsTypeJspTypeType.TEXT_TYPE
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    public void testMemberBuildSQL()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");

        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
//                System.out.println("Module "+mod.getName());

                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

//                if (!mod.getName().equals("content.copyright"))
//                    continue;

                // assumed not empty type is 'lookup'
                if (mod.getType()!=null)
                    continue;

                System.out.println("Start check module "+mod.getName());
                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);

//                    if (content.getAction().getDefaultPortletType()==ContentTypeActionType.INSERT_TYPE)
//                        continue;

                    if (content.getAction().getType()==ContentTypeActionType.INSERT_TYPE)
                    {
                        Exception ee = null;
                        try
                        {
                            String sql = MemberServiceClass.buildInsertSQL(content, null, mod, db_, "remote-user", "server-name");
                            Parser parser = SqlStatement.parseSql(sql);
                        }
                        catch(Exception exc)
                        {
                            ee = exc;
                        }
                        if (isUseAssertion)
                        {
                            System.out.println("ee = " + ee);
                            System.out.println("mf = " + mf);
                            System.out.println("content = " + content);
                            System.out.println("mod = " + mod);
                            assertFalse(
                                    "\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "'\nError build insert SQL: "+
                                    (ee!=null
                                     ?ee.toString()+"\n"+ExceptionTools.getStackTrace(ee, 100)
                                     :"null"),
                                    ee!=null
                            );

                        }
                        else
                        {
                            if (ee!=null)
                                System.out.println(
                                        "\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "'\nError build insert SQL: "+ee.toString()+"\n"+
                                        ExceptionTools.getStackTrace(ee, 100)
                                );
                        }
                    }
                    else if (content.getAction().getType()==ContentTypeActionType.CHANGE_TYPE)
                    {
                        Exception ee = null;
                        try
                        {
//                            HttpServletRequestApplWrapper req = new HttpServletRequestApplWrapper();
//                            PortletRequest portletRequest = new RenderRequestImpl();
                            Map map = new HashMap();
                            String sql = MemberServiceClass.buildUpdateSQL( db_, content, null, mod, true, map, "remote-user", "server-name" );
                            Parser parser = SqlStatement.parseSql(sql);
                        }
                        catch(Exception exc)
                        {
                            ee = exc;
                        }
                        if (isUseAssertion)
                        {
                            assertFalse(
                                    "\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "'\nError build update SQL: "+
                                    (ee!=null
                                     ?ee.toString()+"\n"+ExceptionTools.getStackTrace(ee, 100)
                                     :"null"),
                                    ee!=null
                            );

                        }
                        else
                        {
                            if (ee!=null)
                                System.out.println(
                                        "\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "'\nError build update SQL: "+
                                        ee.toString()+"\n"+
                                        ExceptionTools.getStackTrace(ee, 100)
                                );
                        }
                    }
                    else if (content.getAction().getType()==ContentTypeActionType.DELETE_TYPE)
                    {
                        Exception ee = null;
                        try
                        {
//                            PortletRequest portletRequest = new RenderRequestImpl();
                            Map map = new HashMap();
                            String sql = MemberServiceClass.buildDeleteSQL( db_, mod, content, null, map, "remote-user", "server-name" );
                            Parser parser = SqlStatement.parseSql(sql);
                        }
                        catch(Exception exc)
                        {
                            ee = exc;
                        }
                        if (isUseAssertion)
                        {
                            assertFalse(
                                    "\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "'\nError build delete SQL: "+
                                    (ee!=null
                                     ?ee.toString()+"\n"+ExceptionTools.getStackTrace(ee, 100)
                                     :"null"),
                                    ee!=null
                            );

                        }
                        else
                        {
                            if (ee!=null)
                                System.out.println(
                                        "\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "'\nError build delete SQL: "+ee.toString()+"\n"+
                                        ExceptionTools.getStackTrace(ee, 100)
                                );
                        }
                    }
                }
            }
        }
    }

    // ��������� ������ ����� � QueryArea �� ������� � ���� ������
    public void testMemberRelateClassExists()
            throws Exception
    {
        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
//                System.out.println("Module "+mod.getName());
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

//                if (!"currency.currency".equals(mod.getName()))
//                    continue;

//                System.out.println("Start check module "+mod.getName());
                for (int i = 0; i < mod.getRelateClassCount(); i++)
                {
                    RelateClassType relateClass = mod.getRelateClass(i);
                    String className = relateClass.getClassName();

                    boolean isException = false;
                    Object obj = null;
                    try
                    {
                        obj = MainTools.createCustomObject(className);
                    }
                    catch(Exception ex)
                    {
                        isException = true;
                    }

                    if (isUseAssertion)
                        assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                            "module '" + mod.getName() +
                            "', RelateClass '"+className+"' not exists",
                            isException || obj==null
                        );
                    else
                    {
                        if (isException || obj==null)
                        System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                            "module '" + mod.getName() +
                            "', RelateClass '"+className+"' not exists");
                    }
                }
            }
        }
    }

    // ��������� ������ ����� � QueryArea �� ������� � ���� ������
    public void testRestrict()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");
//
//        System.out.println("Get schema of DB");
//        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );
        DbSchemaType schema = getDbSchema();


        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    for (int t = 0; t < qa.getTableCount(); t++)
                    {
                        TableType table = qa.getTable(t);
//                        if (!"SITE_PORTLET_FAQ".equals(table.getTable()))
//                            continue;

                        DbFieldType dbFieldTemp = null;
                        if (qa.getRestrict()!=null)
                        {
                            switch (qa.getRestrict().getType().getType())
                            {
                                case RestrictTypeTypeType.FIRM_TYPE:
                                    if (DatabaseManager.getTableFromStructure(schema, table.getTable())!=null)
                                    {
                                        dbFieldTemp = DatabaseManager.getFieldFromStructure(schema, table.getTable(), "ID_FIRM");
                                        assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                            "Checked table '"+table.getTable()+"'\n" +
                                            "module '" + mod.getName() +
                                            "', content action '" + content.getAction().toString() +
                                            "' wrong restriction, field 'ID_FIRM' not in DB",
                                            dbFieldTemp==null
                                        );
                                    }
                                    else
                                    {
                                        DbViewType dbViewTemp = DatabaseManager.getViewFromStructure(schema, table.getTable());
                                        if ( dbViewTemp!=null)
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+dbViewTemp.getName()+"' type is 'view'");
                                        else
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+table.getTable()+"' not found");
                                    }
                                    break;
                                case RestrictTypeTypeType.SITE_TYPE:
                                    if (DatabaseManager.getTableFromStructure(schema, table.getTable())!=null)
                                    {
                                        dbFieldTemp = DatabaseManager.getFieldFromStructure(schema, table.getTable(), "ID_SITE");
                                        assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                            "Checked table '"+table.getTable()+"'\n" +
                                            "module '" + mod.getName() +
                                            "', content action '" + content.getAction().toString() +
                                            "' wrong restriction, field 'ID_SITE' not in DB",
                                            dbFieldTemp==null
                                        );
                                    }
                                    else
                                    {
                                        DbViewType dbViewTemp = DatabaseManager.getViewFromStructure(schema, table.getTable());
                                        if ( dbViewTemp!=null)
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+dbViewTemp.getName()+"' type is 'view'");
                                        else
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+table.getTable()+"' not found");
                                    }
                                    break;
                                case RestrictTypeTypeType.USER_TYPE:
                                    if (DatabaseManager.getTableFromStructure(schema, table.getTable())!=null)
                                    {
                                        dbFieldTemp = DatabaseManager.getFieldFromStructure(schema, table.getTable(), "ID_USER");
                                        assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                            "Checked table '"+table.getTable()+"'\n" +
                                            "module '" + mod.getName() +
                                            "', content action '" + content.getAction().toString() +
                                            "' wrong restriction, field 'ID_USER' not in DB",
                                            dbFieldTemp==null
                                        );
                                    }
                                    else
                                    {
                                        DbViewType dbViewTemp = DatabaseManager.getViewFromStructure(schema, table.getTable());
                                        if ( dbViewTemp!=null)
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+dbViewTemp.getName()+"' type is 'view'");
                                        else
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "Checked table '"+table.getTable()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +"'\ncheck restrict '"+qa.getRestrict().getType().toString()+"' impossible - table '"+table.getTable()+"' not found");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }


    // ��������� ������ ����� � QueryArea �� ������� � ���� ������
    public void testMemberTypeFields()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");
//
//        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );
        DbSchemaType schema = getDbSchema();

        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    for (int t = 0; t < qa.getTableCount(); t++)
                    {
                        TableType table = qa.getTable(t);
                        DbTableType dbTable = DatabaseManager.getTableFromStructure(schema, table.getTable());
                        if (dbTable != null)
                        {
                            for (int k = 0; k < qa.getFieldsCount(); k++)
                            {
                                FieldsType field = qa.getFields(k);
/*
                                if (field.getDbType().getDefaultPortletType()==FieldsTypeDbTypeType.NUMBER_TYPE &&
                                    field.getJspType().getDefaultPortletType()==FieldsTypeJspTypeType.TEXT_TYPE)
                                {
                                    System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "' containt field '"+field.getName()+"' with wrong type '"+field.getJspType().toString()+"' ");
                                }
*/
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "' containt field 'IS_CURRENT' with wrong type '"+field.getJspType().toString()+"' ",
                                    field.getDbType().getType()==FieldsTypeDbTypeType.NUMBER_TYPE &&
                                    field.getJspType().getType()==FieldsTypeJspTypeType.TEXT_TYPE
                                );
/*
                                if (field.getJspType().getDefaultPortletType()==FieldsTypeJspTypeType.LOOKUP_TYPE &&
                                    field.getQueryArea()==null)
                                {
                                    System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "' field '"+field.getName()+"' has null QueryArea element");
                                }
                                if (field.getJspType().getDefaultPortletType()==FieldsTypeJspTypeType.LOOKUPCLASS_TYPE&&
                                    field.getClassQuery()==null)
                                {
                                    System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                        "module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "' field '"+field.getName()+"' has null ClassQuery element");
                                }
*/
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "' field '"+field.getName()+"' has null QueryArea element",
                                    field.getJspType().getType()==FieldsTypeJspTypeType.LOOKUP_TYPE &&
                                    field.getQueryArea()==null
                                );
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "' field '"+field.getName()+"' has null ClassQuery element",
                                    field.getJspType().getType()==FieldsTypeJspTypeType.LOOKUPCLASS_TYPE &&
                                    field.getClassQuery()==null
                                );

                                if (field.getJspType().getType()==FieldsTypeJspTypeType.LOOKUPCLASS_TYPE&&
                                    field.getClassQuery()!=null)
                                {

                                    ClassQueryType cq = field.getClassQuery();
                                    for (int kk=0; kk<cq.getParametersCount(); kk++)
                                    {
                                        boolean isExists = false;
                                        String paramField = cq.getParameters(kk).getField();
                                        for (int ii=0; ii<qa.getFieldsCount(); ii++)
                                        {
                                            if (paramField.equals(qa.getFields(ii).getName()))
                                            {
                                                isExists = true;
                                                break;
                                            }
                                        }
                                        assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                            "module '" + mod.getName() +
                                            "', content action '" + content.getAction().toString() +
                                            "' parameter '"+paramField+"' not exists in fields list",
                                            !isExists
                                        );
/*
                                        if (!isExists)
                                        {
                                            System.out.println("\nMember file '"+mf.getFile()+"'\n" +
                                                "module '" + mod.getName() +
                                                "', content action '" + content.getAction().toString() +
                                                "' parameter '"+paramField+"' not exists in fields list");
                                        }
*/
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ��������� ��������� ������ ������� �� ������������� �� ���������� � ���� ������
    public void testLookup()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                String lookupField =mod.getLookupPK();
                if (lookupField==null)
                    continue;

                MemberFile mf1 = member[k1];
                int countLookup = 0;
                Vector v = new Vector();
                for (Enumeration e1 = member[k1].getMemberModules(); e1.hasMoreElements();)
                {
                    ModuleType mod1 = (ModuleType)e1.nextElement();

                    if (mod1.getName().toLowerCase().startsWith("f_d_"))
                        continue;

                    for (int i = 0; i < mod1.getContentCount(); i++)
                    {
                        ContentType content = mod1.getContent(i);
                        QueryAreaType qa = content.getQueryArea();

                        for (int j=0; j<content.getTargetModuleCount(); j++)
                        {
                            try
                            {
                                TargetModuleType target = content.getTargetModule(j);
                                if (target.getAction().getType()== TargetModuleTypeActionType.LOOKUP_TYPE )
                                {
                                    assertTrue("\nName of lookup module not specified, file '"+mf.getFile()+"'\n" +
                                        "' module '" + mod1.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "'",
                                        target.getModule()!=null || target.getModule().length()>0
                                    );
                                }

                                if (target.getAction().getType()== TargetModuleTypeActionType.LOOKUP_TYPE &&
                                    target.getModule().equals(mod.getName()))
                                {
                                    assertTrue("\nPK not equals FK in lookup module, file '"+mf.getFile()+"'\n" +
                                        "FK module '" + mod.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "' PK module '" + mod1.getName() +
                                        "', content action '" + content.getAction().toString() +
                                        "'",
                                        mod.getLookupPK().equals(qa.getPrimaryKey())
                                    );
                                    countLookup++;
                                    v.add("module - '"+mod1.getName() +"', action '" + content.getAction().toString() );
                                }
                            }
                            catch(NullPointerException e11)
                            {
                                System.out.println("\nNPE in file '"+mf.getFile()+"'\n" +
                                    "FK module '" + mod.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "PK module '" + mod1.getName() +
                                    "', content action '" + content.getAction().toString() +
                                    "'"
                                );
                                throw e11;
                            }
                        }
                    }
                    String mainModule = "";
                    for (int t=0; t<v.size(); t++)
                    {
                        mainModule += ((String)v.elementAt(t)+"\n");
                    }
                    assertTrue("\nToo many lookup to module, file '"+mf.getFile()+"'\n" +
                        "FK module '" + mod.getName() + "'" +
                        "\n" + mainModule,
                        countLookup<2
                    );
                }
            }
        }
    }

    // ��������� ��������� ������ ������� �� ������������� �� ���������� � ���� ������
    public void testRestrictFields()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");

//        DbSchemaType schema = DatabaseManager.getDbStructure(db_, "MILLENNIUM");

        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();
                    RestrictType restrict = qa.getRestrict();
                    if (restrict!=null)
                    {
                        boolean isFound = false;
                        for (int t=0; t<qa.getFieldsCount(); t++)
                        {
                            FieldsType field = qa.getFields(t);
                            switch (restrict.getType().getType())
                            {
                                case RestrictTypeTypeType.FIRM_TYPE:
                                    if ( field.getName().equalsIgnoreCase("ID_FIRM"))
                                        isFound = true;

                                    continue;

                                case RestrictTypeTypeType.SITE_TYPE:
                                    if ( field.getName().equalsIgnoreCase("ID_SITE"))
                                        isFound = true;

                                    continue;

                                case RestrictTypeTypeType.USER_TYPE:
                                    if ( field.getName().equalsIgnoreCase("ID_USER"))
                                        isFound = true;

                                    continue;

                                default:
                            }
                        }
                        assertTrue("\nMember file '"+mf.getFile()+"'\n" +
                            "module '" + mod.getName() +
                            "', content action '" + content.getAction().toString() +
                            "' containt invalid restriction, restriction field not found",
                            isFound
                        );
                    }
                }
            }
        }
    }

    public void testMemberTemplateQuery()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        ModuleManager.init();

        ModuleType module = ModuleManager.getModule(TemplateMemberClassQuery.nameModule);
        assertFalse("Module '" + TemplateMemberClassQuery.nameModule + "' not found",
            module == null
        );

        ContentType content = ModuleManager.getContent(
            TemplateMemberClassQuery.nameModule, ContentTypeActionType.INDEX_TYPE);

        assertFalse("Content 'index' in '" + TemplateMemberClassQuery.nameModule + "' module not found",
            content == null
        );

/*
TargetModuleType target = null;
        for (int i=0; i<content.getTargetModuleCount(); i++)
        {
            TargetModuleType check = content.getTargetModule(i);
            if (check.getAction().getDefaultPortletType()==TargetModuleTypeActionType.LOOKUP_TYPE)
            {
                target = check;
                break;
            }
        }
        assertFalse("Content 'index' in '"+TemplateMemberClassQuery.nameModule+"' module not found",
            content==null
        );
*/

        boolean isFound = false;
        for (int i = 0; i < content.getQueryArea().getFieldsCount(); i++)
        {
            FieldsType field = content.getQueryArea().getFields(i);
            if (field.getName().equals(TemplateMemberClassQuery.nameField))
            {
                isFound = true;
                break;
            }
        }
        assertFalse("Field '" + TemplateMemberClassQuery.nameField +
            "' in module '" + TemplateMemberClassQuery.nameModule + "'not found",
            !isFound
        );
    }

    // ����� ��������� ������ ������ ��������� � ������� � QueryArea ���� ��� ��� �������
    public void testPrimaryKeyFieldName()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    String pk = qa.getPrimaryKey();
                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                        "Primary key in module '" + mod.getName() + "', content '" +
                        content.getAction().toString() +
                        "' not specified", (pk == null || pk.length() == 0));

                    boolean isPkNotFound = true;
                    for (int k = 0; k < qa.getFieldsCount(); k++)
                    {
                        FieldsType field = qa.getFields(k);
                        if (pk.equalsIgnoreCase(field.getName()))
                        {
                            assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                "Primary key in module '" + mod.getName() +
                                "', content '" + content.getAction().toString() +
                                "' not equal to name of field in fields list",
                                (!pk.equals(field.getName()))
                            );
                            isPkNotFound = false;
                        }
                    }

                    assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                        "Primary key in module '" + mod.getName() +
                        "', content '" + content.getAction().toString() +
                        "' not present in fields list",
                        isPkNotFound
                    );
                }
            }
        }
    }

    // ��������� ����� �� ����� ������������ ��� ���������� ������� - action=='insert'
    public void testPrimaryKeyInsertIsShow()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    String pk = qa.getPrimaryKey();
                    if (pk != null && pk.length()>0)
                    {
                        for (int k = 0; k < qa.getFieldsCount(); k++)
                        {
                            FieldsType field = qa.getFields(k);
                            if (pk.equalsIgnoreCase(field.getName()))
                            {
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "Primary key in module '" + mod.getName() +
                                    "', content '" + content.getAction().toString() +
                                    "' can not be isShow=true",
                                    Boolean.TRUE.equals(field.getIsShow()) && content.getAction().getType()== ContentTypeActionType.INSERT_TYPE
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    // ����� ��������� ������ �� ������� ��� ��� �� ����� ��������������� ���� ���� isShow==true
    public void testPrimaryKeyIsEdit()
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        ModuleManager.init();

        MemberFile[] member = ModuleManager.getMemberFileArray();

        for (int k1 = 0; k1 < member.length; k1++)
        {
            MemberFile mf = member[k1];
            for (Enumeration e = member[k1].getMemberModules(); e.hasMoreElements();)
            {
                ModuleType mod = (ModuleType)e.nextElement();
                if (mod.getName().toLowerCase().startsWith("f_d_"))
                    continue;

                for (int i = 0; i < mod.getContentCount(); i++)
                {
                    ContentType content = mod.getContent(i);
                    QueryAreaType qa = content.getQueryArea();

                    String pk = qa.getPrimaryKey();
                    if (pk != null && pk.length()>0)
                    {

                        for (int k = 0; k < qa.getFieldsCount(); k++)
                        {
                            FieldsType field = qa.getFields(k);
                            if (pk.equalsIgnoreCase(field.getName()))
                            {
                                assertFalse("\nMember file '"+mf.getFile()+"'\n" +
                                    "Primary key in module '" + mod.getName() +
                                    "', content '" + content.getAction().toString() +
                                    "' can not be (isEdit=true and isShow==true)",
                                    Boolean.TRUE.equals(field.getIsShow()) && Boolean.TRUE.equals(field.getIsEdit()) && content.getAction().getType()== ContentTypeActionType.CHANGE_TYPE
                                );
                            }
                        }
                    }
                }
            }
        }
    }

}