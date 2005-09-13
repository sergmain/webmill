package org.riverock.webmill.container.portlet.bean;

import java.io.Serializable;

/**
 * The display-name type contains a short name that is intended
 * to be displayed by tools. It is used by display-name
 * elements. The display name need not be unique.
 * Example:
 * ...
 * Employee Self Service
 * <p/>
 * It has an optional attribute xml:lang to indicate
 * which language is used in the description according to
 * RFC 1766 (http://www.ietf.org/rfc/rfc1766.txt). The default
 * value of this attribute is English(“en”).
 *
 * @version $Revision$ $Date$
 */
public class DisplayName implements Serializable {
    private static final long serialVersionUID = 30434672384237143L;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * In due course, we should install the relevant ISO 2- and
     * 3-letter
     * codes as the enumerated possible values . . .
     */
    private java.lang.String _lang;


    public DisplayName() {
        super();
        setContent("");
    }


    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     *
     * @return the value of field 'content'.
     */
    public java.lang.String getContent() {
        return this._content;
    }  

    /**
     * Returns the value of field 'lang'. The field 'lang' has the
     * following description: In due course, we should install the
     * relevant ISO 2- and 3-letter
     * codes as the enumerated possible values . . .
     *
     * @return the value of field 'lang'.
     */
    public java.lang.String getLang() {
        return this._lang;
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     *
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'lang'. The field 'lang' has the
     * following description: In due course, we should install the
     * relevant ISO 2- and 3-letter
     * codes as the enumerated possible values . . .
     *
     * @param lang the value of field 'lang'.
     */
    public void setLang(java.lang.String lang) {
        this._lang = lang;
    }
}
