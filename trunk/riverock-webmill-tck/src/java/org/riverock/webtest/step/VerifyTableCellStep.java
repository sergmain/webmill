package org.riverock.webtest.step;

import java.io.IOException;
import java.util.Map;

import com.canoo.webtest.engine.Context;
import com.canoo.webtest.engine.StepExecutionException;
import com.canoo.webtest.engine.StepFailedException;
import com.canoo.webtest.steps.Step;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import org.apache.log4j.Logger;

/**
 * @author Serge Maslyukov
 * 20/11/2005
 *
 * $Id$
 */
public class VerifyTableCellStep extends Step {
    private static final Logger LOG = Logger.getLogger(VerifyTableCellStep.class);

    private Integer col = null;
    private Integer row = null;

    private String colString = null;
    private String rowString = null;

    private String text = null;
    private String resultName = null;
    private String id = null;

    public void doExecute(Context context) throws IOException {
        verifyParameters();

        if (context.getLastResponse() == null) {
            throw new StepExecutionException("No last response available! Is previous invoke missing?", this);
        }

        HtmlElement htmlElement = ((HtmlPage)context.getLastResponse()).getDocumentElement();
        HtmlTable htmlTable = null;
        try {
            htmlTable = (HtmlTable)htmlElement.getHtmlElementById( id );
        }
        catch (ElementNotFoundException e) {
            LOG.error("text of response:" + context.getLastResponse().getWebResponse().getContentAsString() );
            throw new StepFailedException("Table with id '"+id+"' not found", this);
        }

        if (htmlTable==null) {
            throw new StepFailedException("Table with id '"+id+"' not found", this);
        }

        HtmlTableCell cell = htmlTable.getCellAt( row.intValue(), col.intValue() );

        boolean b = false;
        if (cell!=null) {
            final String cellXml = cell.asXml();
            LOG.debug("Text as xml in cell ["+row+":"+col+"] :\n"+ cellXml+"\nSearch for: "+text);
            final int idx = cellXml.indexOf(text);
            LOG.debug("Result of indexOf: "+idx);
            b = (idx!=-1);
        }
        LOG.info( "Result of search substring: " + b);
        setWebtestProperty( resultName, ""+b );
    }

    private void verifyParameters() {
        if (rowString==null) {
            throw new StepExecutionException("row not initialized", this);
        }
        row = new Integer( rowString );
        if (row.intValue() < 0) {
            throw new StepExecutionException("row number must be >=0", this);
        }

        if (colString==null) {
            throw new StepExecutionException("col not initialized", this);
        }
        col = new Integer(colString);
        if (col.intValue() < 0) {
            throw new StepExecutionException("col number must be >=0", this);
        }

        if (text == null || text.length() < 1) {
            throw new StepExecutionException("text must be initialized!", this);
        }
        if (resultName == null || resultName.length() < 1) {
            throw new StepExecutionException("resultName must be initialized!", this);
        }
        if (id == null || id.length() < 1) {
            throw new StepExecutionException("id must be initialized!", this);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public void setRow(String rowString) {
        this.rowString = rowString;
    }

    public void setCol(String colString) {
        this.colString = colString;
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
        map.put("id", id);
        map.put("row", "" + row);
        map.put("col", "" + col);
        map.put("resultName", "" + resultName);
        return map;
    }

    public void expandProperties() {
        super.expandProperties();
        text = expandDynamicProperties(text);
        id = expandDynamicProperties(id);
        colString = expandDynamicProperties(colString);
        rowString = expandDynamicProperties(rowString);
    }
}
