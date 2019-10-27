package org.riverock.gwt.client.commons;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.UIObject;

public class DomUtil {

	/**
	 * Gets an attribute on the specified UIObject's element.
	 * 
	 * @param uio a UIObject
	 * @param name an attribute name, in "camelCase"
	 * @return value the attribute value, or <code>null</code> if the
	 *         attribute is not defined
	 */
	public static String getAttribute(UIObject uio, String name)
	{
		return DOM.getElementProperty(uio.getElement(), name);
	}
	
	/**
	 * Sets an attribute on the specified UIObject's element. 
	 * 
	 * @param uio a UIObject
	 * @param name the attribute name, in "camelCase"
	 * @param value a value
	 */
	public static void setAttribute(UIObject uio, String name, String value)
	{
		DOM.setElementProperty(uio.getElement(), name, value);
	}
}
