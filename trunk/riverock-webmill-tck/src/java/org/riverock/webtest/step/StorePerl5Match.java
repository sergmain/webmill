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
import com.canoo.webtest.engine.StepFailedException;
import com.canoo.webtest.steps.Step;
import org.apache.log4j.Logger;
import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.awk.AwkCompiler;
import org.apache.oro.text.awk.AwkMatcher;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author Serge Maslyukov
 * based on code from jakarta-oro examples.MatcherDemoApplet.java
 * 20/11/2005
 *
 * $Id$
 *
 * This file was donated to webtest community as
 * http://webtest-community.canoo.com/jira/browse/WT-48
 */
public class StorePerl5Match extends Step {
    private static final Logger LOG = Logger.getLogger(StorePerl5Match.class);

    private String fText;
    private String fProperty;
    private int fGroup = 0;

    public void doExecute(Context context) throws IOException {
        verifyParameters();
        initCompiler();

        if (context.getLastResponse() == null) {
            throw new StepExecutionException("No last response available! Is previous invoke missing?", this);
        }

        String text = context.getLastResponse().getWebResponse().getContentAsString();;

        MatchResult result = search(text, fText);

        if (result==null) {
            throw new StepFailedException("No match for regular expression <" + fText + ">", this);
        }

        int numberOfGroups = result.groups();
        if (numberOfGroups <= fGroup) {
            throw new StepFailedException("Group not found: " + fGroup + " (#groups: " + numberOfGroups, this);
        }

        String propertyValue = result.group(fGroup);
        LOG.info("Setting dynamic property <" + fProperty + "> to <" + propertyValue + ">");
        setWebtestProperty(fProperty, propertyValue);
    }

    private MatchResult search(final String text, final String exp) {
        int matchNum, group, caseMask, exprChoice, search;
        MatchResult result = null;
        Pattern pattern;
        PatternMatcherInput input;

        exprChoice = PERL5_EXPRESSION;
        caseMask = CASE_MASK[exprChoice][CASE_SENSITIVE];

        LOG.info("Compiling regular expression.");

        try {
            pattern = compiler[exprChoice].compile(exp, caseMask);
        }
        catch (MalformedPatternException e) {
            throw new StepFailedException("\nMalformed Regular Expression:\n" + e.getMessage(), this);
        }

        search = CONTAINS_SEARCH;
        matchNum = 0;
        LOG.info("Searching");

        if (search == MATCHES_SEARCH) {
            if (matcher[exprChoice].matches(text, pattern))
                throw new StepFailedException("Matches expression not supported", this);
        }
        else {
            input = new PatternMatcherInput(text);

            while (matcher[exprChoice].contains(input, pattern)) {
                int groups;

                result = matcher[exprChoice].getMatch();
                ++matchNum;

                LOG.info("Match " + matchNum + ": " + result.group(0));
                groups = result.groups();

                if (groups > 1) {
                    LOG.info("    Subgroups:");
                    for (group = 1; group < groups; group++) {
                        LOG.info("    " + group + ": " +result.group(group));
                    }
                }
            }

            LOG.info("The input contained " + matchNum + " matches.");
        }
        return result;
    }

    static int CONTAINS_SEARCH = 0, MATCHES_SEARCH = 1;
    static int CASE_SENSITIVE = 0, CASE_INSENSITIVE = 1;

    static int PERL5_EXPRESSION = 0;
    static int AWK_EXPRESSION = 1;
    static int GLOB_EXPRESSION = 2;

    static String[] expressionType = {
        "Perl5 Expression:", "AWK Expression:", "Glob Expression:"
    };

    static int[] CASE_MASK[] = {
        {Perl5Compiler.DEFAULT_MASK,
         Perl5Compiler.CASE_INSENSITIVE_MASK},
        {AwkCompiler.DEFAULT_MASK,
         AwkCompiler.CASE_INSENSITIVE_MASK},
        {GlobCompiler.DEFAULT_MASK,
         GlobCompiler.CASE_INSENSITIVE_MASK}
    };
    PatternCompiler compiler[];
    PatternMatcher matcher[];

    private void initCompiler() {

        compiler = new PatternCompiler[expressionType.length];
        matcher = new PatternMatcher[expressionType.length];

        compiler[PERL5_EXPRESSION] = new Perl5Compiler();
        matcher[PERL5_EXPRESSION] = new Perl5Matcher();

        compiler[AWK_EXPRESSION] = new AwkCompiler();
        matcher[AWK_EXPRESSION] = new AwkMatcher();

        compiler[GLOB_EXPRESSION] = new GlobCompiler();
        matcher[GLOB_EXPRESSION] = matcher[PERL5_EXPRESSION];

    }


    private void verifyParameters() {
        if (fGroup < 0) {
            throw new StepExecutionException("Group number must be >= 0!", this);
        }

        if (fText == null || fText.length() < 1) {
            throw new StepExecutionException("Regular expression must have at least one character!", this);
        }

        if (fProperty == null || fProperty.length() < 1) {
            throw new StepExecutionException("Property name must be at least one character!", this);
        }
    }

    public void setText(String text) {
        fText = text;
    }

    public void setProperty(String property) {
        fProperty = property;
    }

    public void setGroup(int group) {
        fGroup = group;
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
        map.put("text", fText);
        map.put("property", fProperty);
        map.put("group", "" + fGroup);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        fText = expandDynamicProperties(fText);
    }
}
