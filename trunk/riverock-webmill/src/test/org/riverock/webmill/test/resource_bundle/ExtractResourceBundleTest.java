package org.riverock.webmill.test.resource_bundle;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.tools.XmlTools;
import org.riverock.webmill.schema.test.PairListType;
import org.riverock.webmill.schema.test.PairType;

/**
 * @author smaslyukov
 *         Date: 10.08.2005
 *         Time: 16:34:52
 *         $Id$
 */
public class ExtractResourceBundleTest {
    private static final String targetDir = "prop";

    public static void main(String[] args) throws Exception {
        StartupApplication.init();

        PreparedStatement st = null;
        ResultSet rs = null;

        File dir = new File(targetDir);
        if (!dir.exists()) {
            throw new Exception("dir 'prop' not exists");
        }

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            String sql_ =
                "select d.SHORT_NAME_LANGUAGE, d.ID_LANGUAGE " +
                "from   main_language d ";

            st = db_.prepareStatement(sql_);
            rs = st.executeQuery();

            while (rs.next()) {
                String lang = RsetTools.getString(rs, "SHORT_NAME_LANGUAGE");
                int langId = rs.getInt("ID_LANGUAGE");
                processLanguage( db_, lang, langId);
            }
        }
        finally {
            DatabaseManager.close(db_, rs, st);
            db_ = null;
            rs = null;
            st = null;
        }
    }

    private static void processLanguage(DatabaseAdapter db_, String lang, int langId) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String sql_ =
            "select distinct a.NAME_STORAGE, a.ID_MAIN_I18N_STORAGE " +
            "from  main_i18n_storage a, main_i18n_item b, main_i18n_message c " +
            "where a.ID_MAIN_I18N_STORAGE=b.ID_MAIN_I18N_STORAGE and " +
            "b.ID_MAIN_I18N_ITEM=c.ID_MAIN_I18N_ITEM and c.ID_LANGUAGE=? ";

        st = db_.prepareStatement(sql_);
        st.setInt( 1, langId );
        rs = st.executeQuery();

        while (rs.next()) {
            String storage = RsetTools.getString(rs, "NAME_STORAGE");
            int storageId = rs.getInt( "ID_MAIN_I18N_STORAGE");
            processMessage( db_, lang, langId, storage, storageId );
        }
        DatabaseManager.close(rs, st);
        rs = null;
        st = null;

    }

    private static void processMessage(DatabaseAdapter db_, String lang, int langId, String storage, int storageId) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String sql_ =
            "select b.NAME_ITEM, c.MESSAGE " +
            "from   main_i18n_item b, main_i18n_message c " +
            "where  b.ID_MAIN_I18N_STORAGE=? and " +
            "b.ID_MAIN_I18N_ITEM=c.ID_MAIN_I18N_ITEM and c.ID_LANGUAGE=? ";

        st = db_.prepareStatement(sql_);
        st.setInt( 1, storageId );
        st.setInt( 2, langId );
        rs = st.executeQuery();

        PairListType list = new PairListType();

        while (rs.next()) {
            String key = RsetTools.getString(rs, "NAME_ITEM");
            String value = RsetTools.getString(rs, "MESSAGE");
            PairType pair = new PairType();
            pair.setKey(key);
            pair.setValue(value);
            list.addPair( pair );
        }
        DatabaseManager.close(rs, st);
        rs = null;
        st = null;

        String fileNameWithoutExt = StringTools.capitalizeString( storage) + "_"+lang;
        String fileName = targetDir+File.separatorChar+fileNameWithoutExt + ".xml";
        File file =  new File(fileName);
        if (file.exists()) {
            throw new Exception("File "+fileName+" exists. langId: " + langId+ ", storageId: " +storageId );
        }
        XmlTools.writeToFile(list, fileName, "utf-8", "resource");

        String s =
            "/*\n" +
            " * org.riverock.portlet -- Portlet Library\n" +
            " * \n" +
            " * Copyright (C) 2005, Riverock Software, All Rights Reserved.\n" +
            " * \n" +
            " * Riverock -- The Open-source Java Development Community\n" +
            " * http://www.riverock.org\n" +
            " * \n" +
            " * \n" +
            " * This program is free software; you can redistribute it and/or\n" +
            " * modify it under the terms of the GNU General Public\n" +
            " * License as published by the Free Software Foundation; either\n" +
            " * version 2 of the License, or (at your option) any later version.\n" +
            " *\n" +
            " * This library is distributed in the hope that it will be useful,\n" +
            " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\n" +
            " * General Public License for more details.\n" +
            " *\n" +
            " * You should have received a copy of the GNU General Public\n" +
            " * License along with this library; if not, write to the Free Software\n" +
            " * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA\n" +
            " *\n" +
            " */" +
            "\n" +
            "package org.riverock.portlet.resource;\n" +
            "\n" +
            "import java.util.Iterator;\n" +
            "\n" +
            "import org.apache.log4j.Logger;\n" +
            "\n" +
            "import org.riverock.webmill.container.resource.XmlResourceBundle;\n" +
            "\n" +
            "/**\n" +
            " * @author Serge Maslyukov\n" +
            " */\n" +
            "public class "+fileNameWithoutExt+" extends XmlResourceBundle {\n" +
            "    private final static Logger log = Logger.getLogger( "+fileNameWithoutExt+".class );\n" +
            "\n" +
            "    private Object[][] resource = null;\n" +
            "    protected Object[][] getContents() {\n" +
            "        if (resource!=null) {\n" +
            "            return resource;\n" +
            "        }\n" +
            "\n" +
            "        PairList list = null;\n" +
            "        try {\n" +
            "            list = digestXmlFile();\n" +
            "        }\n" +
            "        catch (Exception e) {\n" +
            "            String es = \"Error digest file \" +getFileName();\n" +
            "            log.error(es, e);\n" +
            "            throw new IllegalStateException(es, e);\n" +
            "        }\n" +
            "        resource = new Object[list.getPairs().size()][2];\n" +
            "        Iterator<Pair> iterator = list.getPairs().iterator();\n" +
            "        int i=0;\n" +
            "        while (iterator.hasNext()) {\n" +
            "            Pair pair = iterator.next();\n" +
            "            resource[i][0] = pair.getKey();\n" +
            "            resource[i][1] = pair.getValue();\n" +
            "            i++;\n" +
            "        }\n" +
            "\n" +
            "        return resource;\n" +
            "    }\n" +
            "}";

        MainTools.writeToFile( targetDir+File.separatorChar+fileNameWithoutExt + ".java", s.getBytes() );
    }
}
