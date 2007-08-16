package org.riverock.update.webmill.v581.convert_template;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.apache.commons.lang.StringUtils;

import org.riverock.dbrevision.manager.patch.PatchAction;
import org.riverock.dbrevision.manager.patch.PatchValidator;
import org.riverock.dbrevision.manager.patch.PatchStatus;
import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.utils.DbUtils;
import org.riverock.dbrevision.utils.Utils;
import org.riverock.update.webmill.v581.convert_template.schema.Template;
import org.riverock.update.webmill.v581.convert_template.schema.Portlet;
import org.riverock.update.webmill.v581.convert_template.schema.ElementParameter;
import org.riverock.update.webmill.v581.convert_template.schema.Xslt;
import org.riverock.update.webmill.v581.convert_template.schema.Dynamic;

/**
 * User: SergeMaslyukov
 * Date: 16.08.2007
 * Time: 22:56:49
 */
public class PrepareParsedTemplateField  implements PatchAction, PatchValidator {

    public static final NamespacePrefixMapper NAMESPACE_PREFIX_MAPPER = new NamespacePrefixMapper() {
        public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
            if( "http://webmill.riverock.org/xsd/riverock-template-page-elements.xsd".equals(namespaceUri) )
                return "element";

/*
            // I want the namespace foo to be the default namespace.
            if( "http://www.example.com/foo".equals(namespaceUri) )
                return "";

            // and the namespace bar will use "b".
            if( "http://www.example.com/bar".equals(namespaceUri) )
                return "b";

*/
            // otherwise I don't care. Just use the default suggestion, whatever it may be.
            return suggestion;

        }
    };

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
                        Utils.getObjectFromXml(Template.class, stream);
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
        Statement st = null;

        PatchStatus status = new PatchStatus();
        try {
            st = database.getConnection().createStatement();
            rs = st.executeQuery("select ID_SITE_TEMPLATE from WM_PORTAL_TEMPLATE");
            List<Long> ids = new ArrayList<Long>();
            while (rs.next()) {
                Long templateId = DbUtils.getLong(rs, "ID_SITE_TEMPLATE");
                if (templateId==null) {
                    status.getMessages().addMessage("Db field WM_PORTAL_TEMPLATE.ID_SITE_TEMPLATE must not be null.");
                    status.setStatus(PatchStatus.Status.ERROR);
                    return status;
                }
                ids.add(templateId);
            }
            DbUtils.close(rs, st);
            rs=null;
            st=null;

            for (Long templateId : ids) {
                ps = database.getConnection().prepareStatement(
                    "select TEMPLATE_BLOB from WM_PORTAL_TEMPLATE where ID_SITE_TEMPLATE=?"
                );
                ps.setLong(1, templateId);
                rs = ps.executeQuery();
                byte[] blob=null;
                if (rs.next()) {
                    blob = database.getBlobField(rs, "TEMPLATE_BLOB", 1024*1024);
                }
                DbUtils.close(rs, ps);
                rs=null;
                ps=null;

                if (blob!=null) {
                    try {
                        ByteArrayInputStream stream = new ByteArrayInputStream(blob);

                        Template template = Utils.getObjectFromXml(Template.class, stream);

                        ParsedTemplate parsedTemplate = parseTemplate(template);
                        byte[] bytes = Utils.getXml(parsedTemplate, "ParsedTemplate", "utf-8", true,
                            new NamespacePrefixMapper[] {PrepareParsedTemplateField.NAMESPACE_PREFIX_MAPPER}
                        );
                        updateParsedTemplate(database, templateId, bytes);
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
            status.getMessages().addMessage("Error process template: " + e.toString());
        }
        finally {
            DbUtils.close(rs, st);
            DbUtils.close(ps);
        }
        return status;
    }

    static ParsedTemplate parseTemplate(Template template) {
        for (SiteTemplateItem item : template.getSiteTemplateItem()) {
            if (StringUtils.equals("portlet", item.getType())) {
                Portlet p = new Portlet();
                p.setCode(item.getCode());
                p.setXmlRoot(item.getXmlRoot());
                p.setName(item.getValue());
                p.setTemplate(item.getTemplate());
                template.getDynamicOrPortletOrXslt().add(p);
                for (Parameter parameter : item.getParameter()) {
                    ElementParameter elementParameter = new ElementParameter();
                    elementParameter.setName(parameter.getName());
                    elementParameter.setValue(parameter.getValue());
                    p.getElementParameter().add(elementParameter);
                }
            }
            else if (StringUtils.equals("custom", item.getType())) {
                Xslt c = new Xslt();
                c.setName(item.getValue());
                template.getDynamicOrPortletOrXslt().add(c);
            }
            else if (StringUtils.equals("file", item.getType())) {

            }
            else if (StringUtils.equals("dynamic", item.getType())) {
                Dynamic d = new Dynamic();
                template.getDynamicOrPortletOrXslt().add(d);
            }
            else {
                throw new IllegalStateException("Unknown type of site template item: " + item.getType());
            }
        }
        return template;
    }

    private void updateParsedTemplate(Database database, Long templateId, byte[] bytes) {
        database.setBlobField(
            "WM_PORTAL_TEMPLATE", "PARSED_TEMPLATE", bytes, "ID_SITE_TEMPLATE=?",
            new Object[]{templateId},
            new int[]{Types.INTEGER}
        );
    }
}
