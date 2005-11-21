/*
 * webmill-TCK - webtest based tests for jsr168 TCK
 *
 * Copyright (C) 2005, Riverock Software, All Rights Reserved.
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
 *
 */
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
public class SubtractValueStep extends Step {
    private static final Logger LOG = Logger.getLogger(SubtractValueStep.class);

    private int value = 0;
    private String valueString = null;

    private String prop = null;

    public void doExecute(Context context) throws IOException {
        verifyParameters();

        LOG.info( "Subtract to prop '"+prop+"', value: "+value);
        String oldValue = getWebtestProperty( prop );
        if (oldValue==null || oldValue.length()==0) {
            oldValue = "0";
        }
        int tempValue = Integer.parseInt( oldValue );

        final int newValue = (tempValue - value);
        LOG.debug("old value: "+tempValue+", new value of '"+prop+"': "+newValue);
        setWebtestProperty( prop, ""+newValue );
    }

    private void verifyParameters() {
        if (valueString==null) {
            throw new StepExecutionException("value not initialized", this);
        }
        value = Integer.parseInt( valueString );

        if (prop==null) {
            throw new StepExecutionException("prop not initialized", this);
        }
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public void setValue(String valueString) {
        this.valueString = valueString;
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
        map.put("value", "" + value);
        map.put("prop", "" + prop);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        valueString = expandDynamicProperties(valueString);
    }
}
