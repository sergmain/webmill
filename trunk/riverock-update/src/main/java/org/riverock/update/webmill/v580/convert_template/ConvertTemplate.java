package org.riverock.update.webmill.v580.convert_template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.ByteArrayInputStream;

import org.apache.commons.lang.StringUtils;

import org.riverock.dbrevision.manager.patch.PatchAction;
import org.riverock.dbrevision.db.DatabaseAdapter;
import org.riverock.dbrevision.annotation.schema.db.Action;
import org.riverock.dbrevision.utils.DbUtils;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.update.webmill.v580.convert_template.schema.SiteTemplate;
import org.riverock.update.webmill.v580.convert_template.schema.SiteTemplateItem;
import org.riverock.update.webmill.v580.convert_template.schema.Html;
import org.riverock.update.webmill.v580.convert_template.schema.Template;
import org.riverock.update.webmill.v580.convert_template.schema.Portlet;
import org.riverock.update.webmill.v580.convert_template.schema.Custom;
import org.riverock.update.webmill.v580.convert_template.schema.Dynamic;

/**
 * User: SergeMaslyukov
 * Date: 04.08.2007
 * Time: 2:43:54
 */
public class ConvertTemplate implements PatchAction {
    
    public void processAction(DatabaseAdapter adapter, Action action) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;

        try {
            st = adapter.getConnection().createStatement();
            rs = st.executeQuery("select ID_SITE_TEMPLATE from WM_PORTAL_TEMPLATE");
            while (rs.next()) {
                Long templateId = DbUtils.getLong(rs, "ID_SITE_TEMPLATE");
                if (templateId==null) {
                    throw new IllegalStateException("templateId is null");
                }

                ps = adapter.getConnection().prepareStatement(
                    "select TEMPLATE_BLOB from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE=?"
                );
                ps.setLong(1, templateId);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    byte[] blob = adapter.getBlobField(rs, "TEMPLATE_BLOB", 1024*1024);
                    ByteArrayInputStream stream = new ByteArrayInputStream(blob);

                    SiteTemplate siteTemplate = Utils.getObjectFromXml(SiteTemplate.class, stream);

                    if (StringUtils.isNotBlank(siteTemplate.getRole())) {
                        updateRole(adapter, templateId, siteTemplate.getRole().trim());
                    }
                    Template template = new Template();
                    for (SiteTemplateItem item : siteTemplate.getSiteTemplateItem()) {
                        if (StringUtils.equals("portlet", item.getType())) {
                            Portlet p = new Portlet();
                            p.setCode(item.getCode());
                            p.setXmlRoot(item.getXmlRoot());
                            p.setValue(item.getValue());
                            template.getPortletOrDynamicOrCustom().add(p);
                        }
                        else if (StringUtils.equals("custom", item.getType())) {
                            Custom c = new Custom();
                            c.setValue(item.getValue());
                            template.getPortletOrDynamicOrCustom().add(c);
                        }
                        else if (StringUtils.equals("portlet", item.getType())) {
                            Dynamic d  = new Dynamic();
                            template.getPortletOrDynamicOrCustom().add(d);
                        }
                        else if (StringUtils.equals("file", item.getType())) {

                        }
                        else {
                            throw new IllegalStateException("Unknown type of site template item: " + item.getType());
                        }
                    }
                    byte[] bytes = Utils.getXml(template, "Template", "utf-8");
                    updateTemplate(adapter, templateId, bytes);
                }
            }
            adapter.getConnection().commit();
        }
        finally {
            DbUtils.close(rs, st);
            DbUtils.close(rs1, ps);
        }
    }

    private void updateTemplate(DatabaseAdapter adapter, Long templateId, byte[] bytes) {
        
    }

    private void updateRole(DatabaseAdapter adapter, Long templateId, String role) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
