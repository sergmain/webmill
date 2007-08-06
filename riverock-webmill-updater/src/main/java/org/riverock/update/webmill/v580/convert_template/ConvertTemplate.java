package org.riverock.update.webmill.v580.convert_template;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.manager.patch.PatchAction;
import org.riverock.dbrevision.manager.patch.PatchStatus;
import org.riverock.dbrevision.manager.patch.PatchValidator;
import org.riverock.dbrevision.utils.DbUtils;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.update.webmill.v580.convert_template.schema.Custom;
import org.riverock.update.webmill.v580.convert_template.schema.Dynamic;
import org.riverock.update.webmill.v580.convert_template.schema.Portlet;
import org.riverock.update.webmill.v580.convert_template.schema.SiteTemplate;
import org.riverock.update.webmill.v580.convert_template.schema.SiteTemplateItem;
import org.riverock.update.webmill.v580.convert_template.schema.Template;

/**
 * User: SergeMaslyukov
 * Date: 04.08.2007
 * Time: 2:43:54
 */
public class ConvertTemplate implements PatchAction, PatchValidator {
    
    public PatchStatus validate(Database database) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;

        PatchStatus status = new PatchStatus();
        try {
            st = database.getConnection().createStatement();
            rs = st.executeQuery("select ID_SITE_TEMPLATE from WM_PORTAL_TEMPLATE");
            while (rs.next()) {
                Long templateId = DbUtils.getLong(rs, "ID_SITE_TEMPLATE");
                if (templateId==null) {
                    status.getMessages().addMessage("Db field WM_PORTAL_TEMPLATE.ID_SITE_TEMPLATE must not be null.");
                    status.setStatus(PatchStatus.Status.ERROR);
                    return status;
                }

                ps = database.getConnection().prepareStatement(
                    "select TEMPLATE_BLOB from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE=?"
                );
                ps.setLong(1, templateId);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    byte[] blob = database.getBlobField(rs, "TEMPLATE_BLOB", 1024*1024);
                    ByteArrayInputStream stream = new ByteArrayInputStream(blob);

                    try {
                        Utils.getObjectFromXml(SiteTemplate.class, stream);
                    }
                    catch (JAXBException jaxbException) {
                        status.setStatus(PatchStatus.Status.ERROR);
                        status.getMessages().addMessage("Error parse template with temlateId="+templateId+".");
                        status.getMessages().addMessage("Template:");
                        try {
                            status.getMessages().addMessage(new String(blob, "utf-8"));
                        }
                        catch (UnsupportedEncodingException e) {
                            status.getMessages().addMessage("Error convert template to String, "+e.toString());
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            status.setStatus(PatchStatus.Status.ERROR);
            status.getMessages().addMessage("Error validate templates: " + e.toString());
        }
        finally {
            DbUtils.close(rs, st);
            DbUtils.close(rs1, ps);
        }
        return status;
    }

    public PatchStatus process(Database database) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement st = null;

        PatchStatus status = new PatchStatus();
        try {
            st = database.getConnection().createStatement();
            rs = st.executeQuery("select ID_SITE_TEMPLATE from WM_PORTAL_TEMPLATE");
            while (rs.next()) {
                Long templateId = DbUtils.getLong(rs, "ID_SITE_TEMPLATE");
                if (templateId==null) {
                    status.getMessages().addMessage("Db field WM_PORTAL_TEMPLATE.ID_SITE_TEMPLATE must not be null.");
                    status.setStatus(PatchStatus.Status.ERROR);
                    return status;
                }

                ps = database.getConnection().prepareStatement(
                    "select TEMPLATE_BLOB from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE=?"
                );
                ps.setLong(1, templateId);
                rs1 = ps.executeQuery();
                if (rs1.next()) {
                    try {
                        byte[] blob = database.getBlobField(rs, "TEMPLATE_BLOB", 1024*1024);
                        ByteArrayInputStream stream = new ByteArrayInputStream(blob);

                        SiteTemplate siteTemplate = Utils.getObjectFromXml(SiteTemplate.class, stream);

                        if (StringUtils.isNotBlank(siteTemplate.getRole())) {
                            updateRole(database, templateId, siteTemplate.getRole().trim());
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
                        updateTemplate(database, templateId, bytes);
                    }
                    catch (JAXBException e) {
                        status.setStatus(PatchStatus.Status.ERROR);
                        status.getMessages().addMessage("Error parse template with temlateId="+templateId+".");
                    }
                }
            }
            database.getConnection().commit();
        }
        catch (SQLException e) {
            status.setStatus(PatchStatus.Status.ERROR);
            status.getMessages().addMessage("Error validate templates: " + e.toString());
        }
        finally {
            DbUtils.close(rs, st);
            DbUtils.close(rs1, ps);
        }
        return status;
    }

    private void updateTemplate(Database database, Long templateId, byte[] bytes) {
        
    }

    private void updateRole(Database database, Long templateId, String role) {
        //To change body of created methods use File | Settings | File Templates.
    }
}
