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

 * Date: Nov 19, 2002

 * Time: 9:57:07 PM

 *

 * $Id$

 */

package org.riverock.portlet.member;



import java.util.Locale;

import java.util.StringTokenizer;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.schema.member.*;

import org.riverock.portlet.schema.member.types.ContentTypeActionType;

import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;

import org.riverock.portlet.schema.member.types.ParameterTypeType;

import org.riverock.portlet.schema.member.types.RestrictTypeTypeType;

import org.riverock.portlet.schema.member.types.SqlCheckParameterTypeTypeType;

import org.riverock.portlet.schema.member.types.TargetModuleTypeActionType;

import org.riverock.portlet.schema.member.types.TypeFieldType;

import org.riverock.interfaces.schema.javax.portlet.SecurityRoleRefType;

import org.riverock.generic.tools.XmlTools;

import org.riverock.generic.tools.StringManager;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthException;

import org.riverock.webmill.config.WebmillConfig;



import org.apache.log4j.Logger;



import javax.servlet.http.HttpServletRequest;



class RestrictDescription

{

    public int type;

    public String nameField = null;



    public RestrictDescription(int type_, String nameField_)

    {

        this.type = type_;

        this.nameField = nameField_;

    }

}



public class MemberServiceClass

{



    private static Logger log = Logger.getLogger( "org.riverock.member.MemberServiceClass" );



    private static Object syncDebug = new Object();



    private static RestrictDescription restrictDesc[] =

        {

            new RestrictDescription(RestrictTypeTypeType.FIRM_TYPE, "ID_FIRM"),

            new RestrictDescription(RestrictTypeTypeType.SITE_TYPE, "ID_SITE"),

            new RestrictDescription(RestrictTypeTypeType.USER_TYPE, "ID_USER")

        };



    public static String getString(MultiLangStringType str, Locale loc)

            throws java.io.UnsupportedEncodingException

    {

        return getString(str, loc, "");

    }



    public static String getString(MultiLangStringType str, Locale loc, String defaultString)

            throws java.io.UnsupportedEncodingException

    {

        if (str==null)

            return defaultString;



        if (Boolean.TRUE.equals(str.getIsUseProperties()) )

        {

            StringManager strMan =

                    StringManager.getManager( str.getStorage(), loc);



            return strMan.getStr( str.getStringData() );

        }

        else

        {

            if (Boolean.TRUE.equals(str.getIsInit()))

                return str.getStringData();



            str.setStringData( str.getStringData() == null?defaultString:str.getStringData() );

            str.setIsInit( Boolean.TRUE );

            return str.getStringData();

        }

    }



    public static boolean isUpdateAction( ContentType content)

    {

        for (int k = 0; k < content.getTargetModuleCount(); k++)

        {

            TargetModuleType ta = content.getTargetModule(k);



            if ( ta.getAction().getType() == TargetModuleTypeActionType.CHANGE_TYPE ||

                    ta.getAction().getType() == TargetModuleTypeActionType.DELETE_TYPE ||

                    ta.getAction().getType() == TargetModuleTypeActionType.LOOKUP_TYPE )

            {

                return true;

            }

        }

        return false;

    }



    public static ContentType getContent( ModuleType module, int actionType)

    {

        for(int i=0; i< module.getContentCount();i++)

        {

            ContentType cnt = module.getContent(i);

            if ( cnt.getAction().getType() == actionType )

                return cnt;

        }

        return null;

    }



    public static FieldsType getField( QueryAreaType qa, String nameField)

    {

        if (nameField == null || qa == null || qa.getFieldsCount()==0)

            return null;



        for (int k = 0; k < qa.getFieldsCount(); k++)

        {

            FieldsType ft = qa.getFields(k);

            if ( nameField.equals( ft.getName()) )

                return ft;

        }

        return null;

    }



    public static String getRealName( FieldsType ff )

    {

        return ( ff.getName() );

//        return (ff.nameAlias != null && !"".equals(nameAlias)?

//                nameAlias:nameField);

    }



    public static ModuleType getModule(MemberArea ma, String nameModule_)

    {

        if (nameModule_ == null)

            return null;



        for(int i=0; i<ma.getModuleCount(); i++)

        {

            ModuleType m = ma.getModule(i);

            if (nameModule_.equals(m.getName()))

                return m;

        }

        return null;

/*

        if (log.isDebugEnabled())

        {

            log.debug("#77.005.01 " + (hash != null?"" + hash.size():"hash is null"));

            log.debug("#77.005.02. " + nameModule_);

            log.debug("#77.005.05 " + (v != null?"" + v.size():"v is null"));

        }



        if (nameModule_ == null)

            return null;



        if (hash == null &&  v == null)

            return null;



        if (hash == null || hash.size() != v.size())

        {

            if (log.isInfoEnabled())

                log.info("#77.005.07 reinit hash v -"+v+" hash - "+hash);



            reinitHash();



            if (log.isInfoEnabled())

                log.info("#77.005.09 " + (hash != null?"" + hash.size():"hash is null"));

        }



        return (Module) hash.get(nameModule_);

*/

    }



    public static String buildInsertSQL(

        ContentType content1,

        String fromParam1,

        ModuleType mod1,

        DatabaseAdapter dbDyn )

        throws Exception

    {



        if (log.isDebugEnabled())

        {

            log.debug("Unmarshal sqlCache object");

            synchronized(syncDebug)

            {

                try

                {

                XmlTools.writeToFile(content1.getQueryArea().getSqlCache(),

                    WebmillConfig.getWebmillDebugDir()+"member-content-site-start.xml",

                    "windows-1251");

                }

                catch(Exception ee){}

            }

            log.debug("#7.004.001 SqlCache() "+content1.getQueryArea().getSqlCache());

        }



        if (content1.getQueryArea().getSqlCache() !=null &&

                content1.getQueryArea().getSqlCache().getSql() != null

        )

        {

            String checkSql = content1.getQueryArea().getSqlCache().getSql();



            if (log.isDebugEnabled())

                log.debug("#7.004.005 cached insert sql\n"+ checkSql);



            return checkSql;

        }



        SqlCacheType sqlCacheType = new SqlCacheType();

        sqlCacheType.setIsInit( Boolean.FALSE );

        content1.getQueryArea().setSqlCache( sqlCacheType );



        SqlClause sc = new SqlClause();



        String prevPK = null;

        String prevAlias = null;

        if (fromParam1!=null && fromParam1.length() > 0)

        {

            StringTokenizer st = new StringTokenizer(fromParam1, ",");

            while (st.hasMoreTokens())

            {

                String modName = st.nextToken();



                ModuleType module = ModuleManager.getModule(modName);

                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);



                if (cnt != null && cnt.getQueryArea() != null)

                {

                    SqlClause lookupSc = buildSelectClause(content1, cnt, module);

                    // из lookupSc берем только from (таблицы) и where(услови€)



                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)

                    {

                        if (sc.from.length() != 0)

                            sc.from += ',';



                        sc.from += lookupSc.from;



                        if (log.isDebugEnabled())

                            log.debug("#22.33.01 "+sc.from);



                        if (sc.where.length() != 0)

                            sc.where += " and ";



                        if (log.isDebugEnabled())

                            log.debug("#22.33.02 "+sc.where);



                        sc.where += MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +

                            '.' + cnt.getQueryArea().getPrimaryKey() + "=?";



                        if (log.isDebugEnabled())

                            log.debug("#22.33.07 "+sc.where);



                        // если есть модуль lookup св€зываем текущую таблицу с лукап таблицей

                        if (prevPK != null)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where += prevAlias + prevPK + "=" +

                                MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +

                                '.' + prevPK;



                        }



                        if (log.isDebugEnabled())

                            log.debug("#22.33.09 "+sc.where);



                        if (lookupSc.where.length() > 0)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where += lookupSc.where;

                        }



                        if (log.isDebugEnabled())

                            log.debug("#22.33.11 "+sc.where);



                        prevPK = cnt.getQueryArea().getPrimaryKey();

                        prevAlias = MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';

                    }

                }

                if (log.isDebugEnabled())

                {

                    log.debug("Unmarshal sqlCache object");

                    synchronized(syncDebug)

                    {

                        XmlTools.writeToFile(content1.getQueryArea().getSqlCache(),

                            WebmillConfig.getWebmillDebugDir()+"member-content-site-from-"+module.getName()+".xml",

                            "windows-1251");

                    }

                }

            }

        }



        if (log.isDebugEnabled())

            log.debug("#22.33.41 "+sc.from);



        String insTable = (content1.getQueryArea().getTable(0)).getTable();



// биндим первичный ключ

        String sql_1 = "insert into " + insTable + "(" +

            content1.getQueryArea().getPrimaryKey();



// Bind main variable

        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

        {

            FieldsType ff = content1.getQueryArea().getFields(k);

            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE))

            {

                sql_1 += ',' + ff.getName();

            }

        }



// bind FK to PK of top table

        if (prevPK != null)

        {

            sql_1 += ',' + mod1.getLookupPK();

        }



// обрабатываем селф лукап модуль

        if (mod1.getSelfLookup() != null &&

            mod1.getSelfLookup().getCurrentField() != null &&

            mod1.getSelfLookup().getTopField() != null)

        {

            sql_1 += ',' + mod1.getSelfLookup().getTopField().getName();

        }



        int countQuestion = 0;

// обрабатываем рестрикты

        // если поле, которое используетс€ дл€ ограничений есть уже в списке полей,

        // то ограничение не вставл€етс€, т.к. проверка значени€ будет осуществл€тьс€ в

        // запросе на проверку ограничений

        // ¬Ќ»ћјЌ»≈!. вопросики(биндинг переменны) тоже не должны выставл€тьс€

        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.FIRM_TYPE

            && !checkRestrictField( content1, RestrictTypeTypeType.FIRM_TYPE) )

        {

            sql_1 += ",ID_FIRM";

            countQuestion++;

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.SITE_TYPE

            && !checkRestrictField( content1, RestrictTypeTypeType.SITE_TYPE) )

        {

            sql_1 += ",ID_SITE";

            countQuestion++;

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.USER_TYPE

            && !checkRestrictField( content1, RestrictTypeTypeType.USER_TYPE) )

        {

            sql_1 += ",ID_USER";

            countQuestion++;

        }



        sql_1 += ")";



        String sql_ = "?" + StringTools.getMultypleString(",?", countQuestion);



        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

        {

            FieldsType ff = content1.getQueryArea().getFields(k);

            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE))

            {

                if (ff.getJspType().getType()==FieldsTypeJspTypeType.DATE_TEXT_TYPE)

                    sql_ += ", "+dbDyn.getNameDateBind();

                else

                    sql_ += ",?";

            }

        }



        if (prevPK != null)

        {

            sql_ += ",?";

        }



        if (mod1.getSelfLookup() != null &&

            mod1.getSelfLookup().getCurrentField() != null &&

            mod1.getSelfLookup().getTopField() != null)

        {

            sql_ += ",?";

        }





// вставл€ет 'Restrict' проверки

//        SqlClause restrict = new SqlClause();

//        boolean flag = false;

        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.FIRM_TYPE)

        {

            if (checkRestrictField( content1, RestrictTypeTypeType.FIRM_TYPE))

            {



                String fromTemp = " V$_READ_LIST_FIRM where ID_FIRM=? and USER_LOGIN=? ";

                if (sc.from != null && sc.from.length() > 0)

                     sc.from += ", " + fromTemp;

                else

                    sc.from = fromTemp;



//                restrict.from = " V$_READ_LIST_FIRM";

//                restrict.where = " ID_FIRM=? and USER_LOGIN=? ";



                if (! Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit()) )

                {

                    SqlFromType fromType = new SqlFromType();

                    fromType.setTable( "V$_READ_LIST_FIRM" );

                    fromType.setAlias( null );

                    content1.getQueryArea().getSqlCache().addFrom( fromType );



                    content1.getQueryArea().getSqlCache().addWhere(

                        MemberProcessing.getWhere(

                            null,"ID_FIRM",null,TypeFieldType.FIELD,

                            null,null,"?",TypeFieldType.PARAMETER

                        )

                    );



                    content1.getQueryArea().getSqlCache().addWhere(

                        MemberProcessing.getWhere(

                            null,"USER_LOGIN",null,TypeFieldType.FIELD,

                            null,null,"?",TypeFieldType.PARAMETER

                        )

                    );

                }

            }

            else

            {

//                sql_ += ",?";



//                restrict.select = "ID_FIRM";

//                restrict.from = "AUTH_USER";

//                restrict.where = "USER_LOGIN=?";



                if (! Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit()) )

                {

                    SqlFromType fromType = new SqlFromType();

                    fromType.setTable( "AUTH_USER" );

                    fromType.setAlias( null );

                    content1.getQueryArea().getSqlCache().addFrom( fromType );



                    content1.getQueryArea().getSqlCache().addWhere(

                        MemberProcessing.getWhere(

                            null,"USER_LOGIN",null,TypeFieldType.FIELD,

                            null,null,"?",TypeFieldType.PARAMETER

                        )

                    );

                }

            }

            if (! Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit()) )

            {

                addParameter(

                    content1, "",

                    SqlCheckParameterTypeTypeType.RESTRICT_FIRM,

                    ParameterTypeType.NUMBER

                );

            }

        }



        if (log.isDebugEnabled())

            log.debug("#15.1 restrict.from "+sc.from);



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.SITE_TYPE)

        {

//            sql_ += ", ?";



            if (! Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit()) )

            {

                addParameter(content1, "", SqlCheckParameterTypeTypeType.RESTRICT_SITE );

                SqlFromType fromType = new SqlFromType();

                fromType.setTable( "SITE_VIRTUAL_HOST" );

                fromType.setAlias( MemberProcessing.aliasSiteRestrict );

                content1.getQueryArea().getSqlCache().addFrom( fromType );

                content1.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.aliasSiteRestrict,

                        "NAME_VIRTUAL_HOST",

                        null,

                        TypeFieldType.FIELD,

                        null,

                        null,

                        "lower(?)",

                        TypeFieldType.PARAMETER

                    )

                );

            }

        }



        if (log.isDebugEnabled())

        {

            log.debug("Unmarshal sqlCache object");

            synchronized(syncDebug)

            {

                try

                {

                XmlTools.writeToFile(content1.getQueryArea().getSqlCache(),

                    WebmillConfig.getWebmillDebugDir()+"member-content-site.xml",

                    "windows-1251");

                }

                catch(Exception ee){}

            }

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.USER_TYPE)

        {

//            sql_ += ",?";



            if (! Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit()) )

            {

                SqlFromType fromType = new SqlFromType();

                fromType.setTable( "AUTH_USER" );

                fromType.setAlias( MemberProcessing.aliasUserRestrict );

                content1.getQueryArea().getSqlCache().addFrom( fromType );



                content1.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.aliasUserRestrict,"USER_LOGIN",null,TypeFieldType.FIELD,

                        null,null,"?",TypeFieldType.PARAMETER

                    )

                );

                addParameter(

                    content1, "", SqlCheckParameterTypeTypeType.RESTRICT_USER,

                    ParameterTypeType.NUMBER

                );

            }

        }



// проверка на наличие 'self lookup' записи

        if (mod1.getSelfLookup() != null &&

            mod1.getSelfLookup().getCurrentField() != null &&

            mod1.getSelfLookup().getTopField() != null)

        {



//            flag = true;

        }



        sql_ = "values ("+sql_+")";



        if (log.isDebugEnabled())

        {

            log.debug("#11.1 insert SQL\n"+sql_1+sql_);

            log.debug("#11.2 SqlCache() "+content1.getQueryArea().getSqlCache());

        }



        content1.getQueryArea().getSqlCache().setSql( sql_1+sql_ );

        content1.getQueryArea().getSqlCache().setIsInit( Boolean.TRUE );



        return sql_1+sql_;

    }



    public static boolean checkRestrictField( ContentType content1, int restrictType )

    {

        RestrictDescription desc = null;

        for (int i=0; i<restrictDesc.length; i++)

        {

            if (restrictDesc[i].type == restrictType)

            {

                desc = restrictDesc[i];

                break;

            }

        }



        if ( desc!=null )

        {

            if (desc.nameField.equalsIgnoreCase(content1.getQueryArea().getPrimaryKey()) )

                return true;



            for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

            {

                FieldsType ff = content1.getQueryArea().getFields(k);

                if ( desc.nameField.equalsIgnoreCase(ff.getName()) && Boolean.TRUE.equals(ff.getIsShow()) )

                    return true;

            }

        }

        return false;

    }



    public static SqlClause buildSelectClause(ContentType contentMain, ContentType cnt, ModuleType module)

    {

        SqlClause sc = new SqlClause();



        if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

        {

            addParameterFromPK(contentMain, module.getName() + '.' + cnt.getQueryArea().getPrimaryKey());

            contentMain.getQueryArea().getSqlCache().addWhere(

                MemberProcessing.getWhere(

                    MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),module.getName()),

                    cnt.getQueryArea().getPrimaryKey(),

                    null,

                    TypeFieldType.FIELD,

                    null,

                    null,

                    "?",

                    TypeFieldType.PARAMETER

                )

            );

        }



// строим SELECT выражение из полей

        boolean isAppend = false;

        for (int k = 0; k < cnt.getQueryArea().getFieldsCount(); k++)

        {

            FieldsType ff = cnt.getQueryArea().getFields(k);



//            if (!"bigtext".equals(ff.jspType))

            if (FieldsTypeJspTypeType.BIGTEXT_TYPE != ff.getJspType().getType())

            {

                if (!isAppend)

                    isAppend = true;

                else

                    sc.select += ',';



                sc.select += (

                    MemberProcessing.prepareTableAlias( ff.getRefTable(), module.getName() ) +

                    '.' + ff.getName()

                    );

            }

        }



// строим FROM выражение из названий таблиц

        isAppend = false;

        for (int k = 0; k < cnt.getQueryArea().getTableCount(); k++)

        {

            TableType table = cnt.getQueryArea().getTable(k);

            if (!isAppend)

                isAppend = true;

            else

                sc.from += ',';



            sc.from +=

                table.getTable() + ' ' +

                MemberProcessing.prepareTableAlias(table.getRef(), module.getName());



            if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

            {

                SqlFromType fromType = new SqlFromType();

                fromType.setTable( table.getTable() );

                fromType.setAlias( MemberProcessing.prepareTableAlias(table.getRef(), module.getName() ) );

                contentMain.getQueryArea().getSqlCache().addFrom( fromType );

            }

        }



// если в модуле примен€етс€ виртуальное удаление, используем поле is_deleted

        if (! Boolean.TRUE.equals(module.getIsDelete()) )

        {

            sc.where = MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),

                module.getName()) + ".IS_DELETED=0 ";



            if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

            {

                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),module.getName()),

                        "IS_DELETED",

                        null, TypeFieldType.FIELD,

                        null,

                        null,

                        "0", TypeFieldType.DATA

                    )

                );

            }

        }



// ƒобавление ограничений

// - по фирме

        if (cnt.getQueryArea().getRestrict() != null &&

            cnt.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.FIRM_TYPE)

        {

            sc.from += ", v$_read_list_firm " +

                MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()) + ' ';



            if (sc.where.length() != 0)

                sc.where += " and ";



            sc.where += MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),

                module.getName()) +

                ".ID_FIRM=" + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()) +

                ".ID_FIRM and " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()) +

                ".user_login=? ";



            if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

            {

                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_FIRM );



                SqlFromType fromType = new SqlFromType();

                fromType.setTable( "v$_read_list_firm" );

                fromType.setAlias( MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()) );

                contentMain.getQueryArea().getSqlCache().addFrom( fromType );



                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),module.getName()),

                        "ID_FIRM",

                        null, TypeFieldType.FIELD,

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()),

                        "ID_FIRM",

                        null, TypeFieldType.FIELD

                    )

                );

                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, module.getName()),

                        "USER_LOGIN",

                        null,

                        TypeFieldType.FIELD,

                        null,

                        null,

                        "?",

                        TypeFieldType.PARAMETER

                    )

                );

            }



        }



// - по сайту

        if (cnt.getQueryArea().getRestrict() != null &&

            cnt.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.SITE_TYPE)

        {

            sc.from += ", SITE_VIRTUAL_HOST " +

                MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()) + ' ';



            if (sc.where.length() != 0)

                sc.where += " and ";



            sc.where += MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) +

                ".ID_SITE=" + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()) +

                ".ID_SITE and " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()) + ".name_virtual_host=lower(?) ";



            if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

            {

                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_SITE );



                SqlFromType fromType = new SqlFromType();

                fromType.setTable( "SITE_VIRTUAL_HOST" );

                fromType.setAlias( MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()) );

                contentMain.getQueryArea().getSqlCache().addFrom( fromType );



                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()),

                        "ID_SITE",

                        null,

                        TypeFieldType.FIELD,

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()),

                        "ID_SITE",

                        null,

                        TypeFieldType.FIELD

                    )

                );

                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, module.getName()),

                        "NAME_VIRTUAL_HOST",

                        null,

                        TypeFieldType.FIELD,

                        null,

                        null,

                        "lower(?)",

                        TypeFieldType.PARAMETER

                    )

                );

            }

        }



// - по пользователю

        if (cnt.getQueryArea().getRestrict() != null &&

            cnt.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.USER_TYPE)

        {

            sc.from += ", auth_user " +

                MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()) + ' ';



            if (sc.where.length() != 0)

                sc.where += " and ";



            sc.where += MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) +

                ".id_user=" + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()) +

                ".id_user and " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()) + ".user_login=? ";



            if (! Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit()) )

            {

                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_USER );



                SqlFromType fromType = new SqlFromType();

                fromType.setTable( "AUTH_USER" );

                fromType.setAlias( MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()) );

                contentMain.getQueryArea().getSqlCache().addFrom( fromType );





                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(),module.getName()),

                        "ID_USER",

                        null,

                        TypeFieldType.FIELD,

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()),

                        "ID_USER",

                        null,

                        TypeFieldType.FIELD

                    )

                );

                contentMain.getQueryArea().getSqlCache().addWhere(

                    MemberProcessing.getWhere(

                        MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, module.getName()),

                        "USER_LOGIN",

                        null,

                        TypeFieldType.FIELD,

                        null,

                        null,

                        "?",

                        TypeFieldType.PARAMETER

                    )

                );

            }

        }



// объедин€ем текущее условие WHERE c условием WHERE из LOOKUP модул€

        if (cnt.getQueryArea().getWhere() != null && cnt.getQueryArea().getWhere().length() > 0)

        {

            if (log.isDebugEnabled())

                log.debug("объедин€ем текущее условие WHERE c условием WHERE из LOOKUP модул€");



            if (sc.where.length() > 0)

                sc.where += " and ";



            sc.where += cnt.getQueryArea().getWhere();

        }



// строим ORDER выражение

        isAppend = false;

        for (int k = 0; k < cnt.getQueryArea().getOrderCount(); k++)

        {

            SortOrderType order = cnt.getQueryArea().getOrder(k);

            if (!isAppend)

                isAppend = true;

            else

                sc.order += ',';



// create alias for 'order' fields

            String orderAlias = null;

            for (int j = 0; j < cnt.getQueryArea().getFieldsCount(); j++)

            {

                FieldsType ff = cnt.getQueryArea().getFields(j);

                if (order.getField().equals( ff.getName() ))

                {

                    orderAlias =

                            MemberProcessing.prepareTableAlias( ff.getRefTable(), module.getName() ) + '.';

                    break;

                }

            }

            if (orderAlias==null)

            {

                if (log.isInfoEnabled())

                    log.info("Looking for order field '"+order.getField()+"' failed. May be need check member file");



                orderAlias = "";

            }



            sc.order += (orderAlias + order.getField() + ' ' + (order.getDirection() != null ? order.getDirection().toString() : ""));

        }



        if (log.isDebugEnabled())

        {

            log.debug("buildSelectClause, select - "+sc.select);

            log.debug("buildSelectClause, from - "+sc.from);

            log.debug("buildSelectClause, where - "+sc.where);

            log.debug("buildSelectClause, order - "+sc.order);

        }



        return sc;

    }



    public static void addParameterFromPK(ContentType content, String name)

    {

        SqlCheckParameterType checkParameterType = new SqlCheckParameterType();

        checkParameterType.setParameter( name );



        ParameterTypeType type =

            ParameterTypeType.valueOf(

                content.getQueryArea().getPrimaryKeyType().toString()

            );



        checkParameterType.setParameterType( type );

        checkParameterType.setType( SqlCheckParameterTypeTypeType.HTTP_REQUEST );

        content.getQueryArea().getSqlCache().addCheckParam( checkParameterType );

    }



    public static void addParameter(ContentType content, String name, SqlCheckParameterTypeTypeType typeParam )

    {

        addParameter( content, name, typeParam, ParameterTypeType.STRING );

    }



    private static void addParameter(

        ContentType content,

        String name,

        SqlCheckParameterTypeTypeType typeRestrictParam,

        ParameterTypeType typeParam)

    {

        SqlCheckParameterType checkParameterType = new SqlCheckParameterType();

        content.getQueryArea().getSqlCache().addCheckParam( checkParameterType );



        checkParameterType.setParameter( name );

        checkParameterType.setParameterType( typeParam );

        checkParameterType.setType( typeRestrictParam );

    }



    public static boolean checkRole( AuthSession auth_, ContentType content_ ) throws AuthException

    {

        if (content_==null || auth_==null)

            return false;



        for (int i=0; i<content_.getSecurityRoleRefCount(); i++)

        {

             SecurityRoleRefType role = content_.getSecurityRoleRef(i);

            if (auth_.isUserInRole( role.getRoleName()) )

                return true;

        }

        return false;

    }



    public static String buildUpdateSQL(ContentType content1, String fromParam1, ModuleType mod1, DatabaseAdapter dbDyn, boolean isUsePrimaryKey, HttpServletRequest request)

        throws Exception

    {

        if (content1 == null || content1.getQueryArea() == null)

            return null;

/*

if (mod.updateCommitSQL != null)

{

log.debug("#5.004.001 return cached 'change' commit sql ");

return mod.selectSQL;

}

*/



        String insTable = (content1.getQueryArea().getTable(0)).getTable();



        String sql_ = "update " + insTable + ' ' +

//            prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +

            " set ";



        boolean isNotComma = true;



        // ѕровер€ем, что если есть хоть одно поле YES_1_NO_N и этот запрос выполн€ет установку данного пол€ в false

        if (hasYesNoField(request, mod1, content1) && !isUsePrimaryKey)

        {

            if (log.isDebugEnabled())

                log.debug("Build update clause for YES_1_NO_N");



            for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

            {

                FieldsType ff = content1.getQueryArea().getFields(k);

                if ( Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) &&

                        (ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE)

                )

                {

                    if (isNotComma)

                        isNotComma = false;

                    else

                        sql_ += ',';



                    sql_ +=

//                        prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                        ff.getName() + "=0";

                }

            }

        }

        else

        {

            for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

            {

                FieldsType ff = content1.getQueryArea().getFields(k);

                if ( Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) &&

                        (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)

                )

                {

                    if (isNotComma)

                        isNotComma = false;

                    else

                        sql_ += ',';



                    sql_ += (ff.getName()+"=");

                    if (ff.getJspType().getType()==FieldsTypeJspTypeType.DATE_TEXT_TYPE)

                        sql_ += dbDyn.getNameDateBind();

                    else

                        sql_ += "?";

                }

            }

        }

        String where_ = "";



        SqlClause sc = new SqlClause();

        SqlClause lookupSc = null;



        String prevPK = null;

        String prevAlias = null;

        if (fromParam1!=null && fromParam1.length() > 0)

        {

            StringTokenizer st = new StringTokenizer(fromParam1, ",");



            while (st.hasMoreTokens())

            {

                String modName = st.nextToken();



                ModuleType module = ModuleManager.getModule(modName);

                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);

                if (cnt != null && cnt.getQueryArea() != null)

                {

                    lookupSc = buildSelectClause(content1, cnt, module);



                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)

                    {

                        if (sc.from.length() != 0)

                            sc.from += ',';



                        sc.from += lookupSc.from;



                        if (sc.where.length() != 0)

                            sc.where += " and ";



                        sc.where +=

                            MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' +

                            cnt.getQueryArea().getPrimaryKey() + "=?";



                        if (prevPK != null)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where +=

                                prevAlias + prevPK + "=" +

                                MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' +

                                prevPK;



                        }



                        if (lookupSc.where.length() > 0)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where += lookupSc.where;

                        }



                        prevPK = cnt.getQueryArea().getPrimaryKey();

                        prevAlias = MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';



                    } //  if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)

                }

            }



            if (prevPK != null)

            {

                if (where_.length() > 0)

                    where_ += " and ";



                where_ += (

//                    prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                    prevPK + " in ( select " + prevAlias + prevPK +

                    " from " + sc.from + " where " + sc.where + ")"

                    );



            }

        }



        // дл€ обработки полей yes_1_no_n первичный ключ не используетс€.

        if (isUsePrimaryKey)

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                content1.getQueryArea().getPrimaryKey() + "=?";

        }



        if (! Boolean.TRUE.equals(mod1.getIsDelete()) )

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.'+

                "is_deleted=0";

        }



        if (mod1.getSelfLookup() != null &&

            mod1.getSelfLookup().getCurrentField() != null &&

            mod1.getSelfLookup().getTopField() != null)

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(),mod.getName()) +'.' +

                mod1.getSelfLookup().getTopField().getName() + "=?";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE)

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                "ID_FIRM in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) +

                '.' + "ID_FIRM " +

                "  from v$_read_list_firm " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) + '.' + "user_login = ? ) ";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE)

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                "ID_SITE in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                '.' + "ID_SITE " +

                "  from site_virtual_host " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                ".NAME_VIRTUAL_HOST = lower(?) ) ";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)

        {

            if (where_.length() > 0)

                where_ += " and ";



            where_ +=

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                "id_user in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +'.' +

                "id_user " +

                "  from auth_user " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +

                ".user_login=? ) ";

        }



        SqlCacheType sqlCacheType = new SqlCacheType();

        sqlCacheType.setIsInit( Boolean.FALSE );

        content1.getQueryArea().setSqlCache( sqlCacheType );



        return sql_ + (where_ != null && where_.trim().length()>0? (" where " + where_): "" );

    }



    public static boolean hasYesNoField(HttpServletRequest req1, ModuleType mod1, ContentType content1)

    {

        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

        {

            FieldsType ff = content1.getQueryArea().getFields(k);



            if (ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE &&

                    new Integer(1).equals(ServletTools.getInt(req1, mod1.getName() + '.' + getRealName(ff)))

            )

            {

                if (log.isDebugEnabled())

                    log.debug("yes1-noN field - "+mod1.getName() + '.' + getRealName(ff)+

                            " value - "+ServletTools.getInt(req1, mod1.getName() + '.' + getRealName(ff)));



                return true;

            }



        }

        return false;

    }



    public static String buildDeleteSQL(ContentType content1, ModuleType mod1, String fromParam1)

        throws Exception

    {

        String insTable = content1.getQueryArea().getTable(0).getTable();



        String sql_ = null;

        if ( Boolean.TRUE.equals(mod1.getIsDelete()) )

        {

            sql_ = "delete from " + insTable + ' ';

//                +prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName());

        }

        else

        {

            sql_ = "update " + insTable + ' ' +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +

                " set " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                "is_deleted=1 ";

        }



        String where_ = "";



        SqlClause sc = new SqlClause();

        SqlClause lookupSc = null;



        String prevPK = null;

        String prevAlias = null;

        if (fromParam1!=null && fromParam1.length() > 0)

        {

            StringTokenizer st = new StringTokenizer(fromParam1, ",");



            while (st.hasMoreTokens())

            {

                String modName = st.nextToken();



                ModuleType module = ModuleManager.getModule(modName);



                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);

                if (cnt != null && cnt.getQueryArea() != null)

                {

                    lookupSc = buildSelectClause(content1, cnt, module);



                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0)

                    {

                        if (sc.from.length() != 0)

                            sc.from += ',';



                        sc.from += lookupSc.from;



                        if (sc.where.length() != 0)

                            sc.where += " and ";



                        sc.where += MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +

                            '.' + cnt.getQueryArea().getPrimaryKey() + "=?";



                        if (prevPK != null)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where += prevAlias + prevPK + "=" + MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) +

                                '.' + prevPK;



                        }



                        if (lookupSc.where.length() > 0)

                        {

                            if (sc.where.length() != 0)

                                sc.where += " and ";



                            sc.where += lookupSc.where;

                        }



                        prevPK = cnt.getQueryArea().getPrimaryKey();

                        prevAlias = MemberProcessing.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';

                    }

                }

            }



            if (prevPK != null)

            {

                if (where_.length() > 0)

                    where_ += " and ";



                where_ +=

//                    prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                    prevPK + " in ( select " + prevAlias + prevPK +

                    " from " + sc.from + " where " + sc.where + ")";



            }

        }



        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++)

        {

            FieldsType ff = content1.getQueryArea().getFields(k);



            if ( ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE )

            {

                if (log.isDebugEnabled())

                    log.debug("yes1-noN field - "+mod1.getName() + '.' + getRealName(ff));



                if (where_.length() > 0)

                    where_ += " and ";

                where_ +=

//                    prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                    ff.getName() + "=0";



            }

        }



        if (where_.length() > 0)

            where_ += " and ";



        where_ +=

//            prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

            content1.getQueryArea().getPrimaryKey() + "=?";



        if (! Boolean.TRUE.equals(mod1.getIsDelete()) )

            where_ += " and " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.'+

                "is_deleted = 0";



        if (mod1.getSelfLookup() != null &&

            mod1.getSelfLookup().getCurrentField() != null &&

            mod1.getSelfLookup().getTopField() != null)

        {

            where_ += " and " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                mod1.getSelfLookup().getTopField().getName() + "=?";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.FIRM_TYPE)

        {

            where_ += " and " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                "ID_FIRM in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) +

                '.' + "ID_FIRM " +

                "  from v$_read_list_firm " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasFirmRestrict, mod1.getName()) + '.' + "USER_LOGIN=? ) ";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() ==

            RestrictTypeTypeType.SITE_TYPE)

        {

            where_ += " and " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) + '.' +

                "ID_SITE in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                '.' + "ID_SITE " +

                "  from site_virtual_host " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasSiteRestrict, mod1.getName()) +

                ".NAME_VIRTUAL_HOST=lower(?) ) ";

        }



        if (content1.getQueryArea().getRestrict() != null &&

            content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE)

//        if (content.getQueryArea().isRestrict("user"))

        {

            where_ += " and " +

//                prepareTableAlias(content.getQueryArea().getMainRefTable(), mod.getName()) +'.' +

                "ID_USER in " +

                " (select " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +

                '.' + "ID_USER " +

                "  from AUTH_USER " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +

                "  where " + MemberProcessing.prepareTableAlias(MemberProcessing.aliasUserRestrict, mod1.getName()) +

                ".USER_LOGIN=?) ";

        }



        return sql_ + (where_ != null && where_.trim().length()>0? (" where " + where_): "" );

//        return sql_ + " where " + where_;

/*

String insTable = ((TableType)content.getQueryArea().tables.elementAt(0)).table;

String where_ = " where "+

(content.getQueryArea().getMainRefTable()!=null?content.getQueryArea().getMainRefTable()+'.':"")+

content.getQueryArea().getPrimaryKey() + "=?";



if (content.getQueryArea().isRestrict( "firm" ))

{

where_ += " and "+content.getQueryArea().getMainRefTable()+'.'+"ID_FIRM in "+

" (select "+aliasFirmRestrict+'.'+"ID_FIRM "+

"  from v$_read_list_firm "+aliasFirmRestrict+

"  where "+aliasFirmRestrict+'.'+"user_login = ? ) ";

}



return sql_ + where_;

*/

    }



}

