package org.riverock.portlet.jsf.validator;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

/**
 * User: SMaslyukov
 * Date: 17.07.2007
 * Time: 17:13:02
 */
public class TextValidatorTag extends ValidatorTag {
    private static Logger log = Logger.getLogger(TextValidatorTag.class);

    private String type;
    
    private static final String VALIDATOR_ID = "TextValidator";

    protected Validator createValidator() throws JspException {
        log.debug("Start createValidator(), type: " + type);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        setValidatorId(VALIDATOR_ID);

        TextValidator validator = (TextValidator) super.createValidator();

        if (type != null) {
            if (UIComponentTag.isValueReference(type)) {
                ValueBinding vb = facesContext.getApplication().createValueBinding(type);
                validator.setType(vb.getValue(facesContext).toString());
            }
            else {
                validator.setType(type);
            }
        }
        return validator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        log.debug("setType(), type: " + type);
        this.type = type;
    }
}
