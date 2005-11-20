package org.riverock.webtest.step;

import java.io.IOException;
import java.util.Map;

import com.canoo.webtest.engine.Context;
import com.canoo.webtest.engine.StepExecutionException;
import com.canoo.webtest.steps.Step;
import org.apache.log4j.Logger;

/**
 * @author Serge Maslyukov
 * 20/11/2005
 *
 * $Id$
 */
public class ExceptionStep extends Step {
    private String text = null;

    public void doExecute(Context context) throws IOException {
        if (text==null || text.length()==0) {
            text="Break operation;";
        }
        throw new StepExecutionException(text, this);
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Collect parameters for reporting. Our our parameters to the ones obtained
     * from super.
     *
     * @return a HashMap containing paramter names (key) and their associated
     *         values
     */
    public Map getParameterDictionary() {
        Map map = super.getParameterDictionary();
        map.put("text", text);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        text = expandDynamicProperties(text);
    }
}
