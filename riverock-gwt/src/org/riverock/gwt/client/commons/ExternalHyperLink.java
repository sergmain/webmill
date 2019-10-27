package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * An anchor linking to a page (or resource) external to the application. 
 * Clicking the hyperlink will result in the GWT application being 
 * unloaded unless you specify a target frame, window or "_blank". 
 * 
 * <h3>Example usage</h3>
 * <p>
 * <pre>
 *   new ExternalHyperLink("Google", "http://www.google.com");
 *   new ExternalHyperLink("Go to &lt;b&gt;Google&lt;/b&gt;", true, "http://www.google.com", "contentFrame");
 *   new ExternalHyperLink("More info", false, "moreInfo.html", ExternalHyperLink.TARGET_BLANK);
 *   new ExternalHyperLink("Email us for more info", "mailto:sales@example.com");
 * </pre>
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.tk-ExternalHyperLink { }</li>
 * </ul>
 * 
 * Код взят отсюда - http://asquare.net/gwttk/
 */
public class ExternalHyperLink extends Widget implements HasText, HasHTML
{
	/**
	 * Specify this target to open the linked location in a new window. 
	 */
	public static final String TARGET_BLANK = "_blank";
	
	/**
	 * Specify this target to open the linked location in this frame's parent frame. 
	 */
	public static final String TARGET_PARENT = "_parent";

	/**
	 * Specify this target to open the linked location in this frame. 
	 */
	public static final String TARGET_SELF = "_self";
	
	/**
	 * Specify this target to open the linked location in the root frame. 
	 */
	public static final String TARGET_TOP = "_top";
	
	/**
	 * Constructs a new ExternalHyperLink
	 * 
	 * @param text a String to display in the link
	 * @param url the url the link will open
	 */
	public ExternalHyperLink(String text, String url)
	{
		this(text, false, url, null);
	}
	
	/**
	 * Constructs a new ExternalHyperLink
	 * 
	 * @param text a String to display in the link
	 * @param asHtml <code>true</code> to treat <code>text</code> as HTML,
	 *            <code>false</code> to treat <code>text</code> as plain
	 *            text
	 * @param url the url the link will open
	 * @param target the case-sensitive name of a target frame or window, or one
	 *            of the reserved target constants
	 */
	public ExternalHyperLink(String text, boolean asHtml, String url, String target)
	{
		setElement(DOM.createAnchor());
		setStyleName("tk-ExternalHyperLink");
		
		if (text != null)
		{
			if (asHtml)
			{
				setHTML(text);
			}
			else
			{
				setText(text);
			}
		}
		
		if (url != null)
		{
			setUrl(url);
		}
		
		if (target != null)
		{
			setTarget(target);
		}
	}
	
	/**
	 * Get the url which the link will open. 
	 */
	public String getUrl()
	{
		return DomUtil.getAttribute(this, "href");
	}
	
	/**
	 * Set the url which the link will open. 
	 */
	public void setUrl(String url)
	{
		DomUtil.setAttribute(this, "href", url);
	}
	
	/**
	 * Get the target frame or window in which the link will open. 
	 */
	public String getTarget()
	{
		return DomUtil.getAttribute(this, "target");
	}
	
	/**
	 * Set the target frame or window in which the link will open.
	 * 
	 * @param target the case-sensitive name of a target frame or window, or one
	 *            of the reserved target constants
	 */
	public void setTarget(String target)
	{
		DomUtil.setAttribute(this, "target", target);
	}
	
	// HasText methods
	public String getText()
	{
		return DOM.getInnerText(getElement());
	}
	
	public void setText(String text)
	{
		DOM.setInnerText(getElement(), text);
	}
	
	// HasHTML methods
	public String getHTML()
	{
		return DOM.getInnerHTML(getElement());
	}

	public void setHTML(String html)
	{
		DOM.setInnerHTML(getElement(), html);		
	}
}
