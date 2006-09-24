/*
 * webmill-TCK - webtest based tests for jsr168 TCK
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
public class EqualsStep extends Step {
    private static final Logger LOG = Logger.getLogger(EqualsStep.class);

    private String value1 = null;
    private String value2 = null;

    private String prop = null;

    public void doExecute(Context context) throws IOException {
        verifyParameters();

        final boolean b = value1.equals(value2);
        LOG.debug("value1: "+value1+", value2: "+value2+", result of compare: "+b);
        setWebtestProperty( prop, ""+b );
    }

    private void verifyParameters() {
        if (value1==null) {
            throw new StepExecutionException("value1 not initialized", this);
        }

        if (value2==null) {
            throw new StepExecutionException("value2 not initialized", this);
        }

        if (prop==null) {
            throw new StepExecutionException("prop not initialized", this);
        }
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public void setValue1(String valueString) {
        this.value1 = valueString;
    }

    public void setValue2(String valueString) {
        this.value2 = valueString;
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
        map.put("value1", "" + value1);
        map.put("value2", "" + value2);
        map.put("prop", "" + prop);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        value1 = expandDynamicProperties(value1);
        value2 = expandDynamicProperties(value2);
    }
}
