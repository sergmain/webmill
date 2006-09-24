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
public class TextOnPageStep extends Step {
    private static final Logger LOG = Logger.getLogger(TextOnPageStep.class);

    private String search = null;

    private String prop = null;

    public void doExecute(Context context) throws IOException {
        verifyParameters();

        String textResponse = context.getLastResponse().getWebResponse().getContentAsString();;
        boolean b = false;
        if (textResponse!=null && textResponse.indexOf(search)!=-1) {
            b = true;
        }
        LOG.info( search );
        setWebtestProperty( prop, ""+b );
    }

    private void verifyParameters() {
        if (prop==null) {
            throw new StepExecutionException("prop not initialized", this);
        }
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public void setSearch(final String search) {
        this.search = search;
        if (this.search==null) {
            this.search = "";
        }
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
        map.put("search", search);
        map.put("prop", "" + prop);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        LOG.info( "search before expand: " + search );
        search = expandDynamicProperties(search);
        LOG.info( "search after expand: " + search );
    }
}
