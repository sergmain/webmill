/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 */

/*
 * This class is based on a class originally written by Jason Hunter
 * <jhunter@acm.org> as part of the book "Java Servlet Programming"
 * (O'Reilly).  See http://www.servlets.com/book for more information.
 * Used by Sun Microsystems with permission.
 */

package org.riverock.common.contenttype;


import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;



/**
 * Utility class that attempts to map from a Locale to the corresponding
 * character set to be used for interpreting input text (or generating
 * output text) when the Content-Type header does not include one.  You
 * can customize the behavior of this class by modifying the mapping data
 * it loads, or by subclassing it (to change the algorithm) and then using
 * your own version for a particular web application.
 *
 */
public class CharsetMapper {


    // ---------------------------------------------------- Manifest Constants


    /**
     * Default properties resource name.
     */
    public static final String DEFAULT_RESOURCE =
      "/org/riverock/common/contenttype/CharsetMapperDefault.properties";


    // ---------------------------------------------------------- Constructors


    /**
     * Construct a new CharsetMapper using the default properties resource.
     */
    public CharsetMapper() {

        this(DEFAULT_RESOURCE);

    }


    /**
     * Construct a new CharsetMapper using the specified properties resource.
     *
     * @param name Name of a properties resource to be loaded
     *
     * @exception IllegalArgumentException if the specified properties
     *  resource could not be loaded for any reason.
     */
    public CharsetMapper(String name) {

        try {
            InputStream stream = CharsetMapper.class.getResourceAsStream(name);
            map.load(stream);
            stream.close();
        } catch (Throwable t) {
	    String es = "Error create CharsetMapper object";
            throw new IllegalArgumentException( es, t );
        }


    }


    // ---------------------------------------------------- Instance Variables


    /**
     * The mapping properties that have been initialized from the specified or
     * default properties resource.
     */
    private Properties map = new Properties();




    // ------------------------------------------------------- Public Methods


    /**
     * Calculate the name of a character set to be assumed, given the specified
     * Locale and the absence of a character set specified as part of the
     * content type header.
     *
     * @param locale The locale for which to calculate a character set
     */
    public String getCharset(Locale locale) {

        String charset = null;

        // First, try a full name match (language, country, variant)
        charset = map.getProperty(locale.toString());
        if (charset != null)
            return (charset);

        // Second, try to match just the language and country
        charset = map.getProperty(
            new StringBuilder(locale.getLanguage()).append( '_' ).append( locale.getCountry() ).toString()
        );
        if (charset!=null)
            return (charset);

        // 3rd, we try only language
        charset = map.getProperty(locale.getLanguage());
        return (charset);

    }

    /**
     * The deployment descriptor can have a
     * locale-encoding-mapping-list element which describes the
     * webapp's desired mapping from locale to charset.  This method
     * gets called when processing the web.xml file for a context
     *
     * @param locale The locale for a character set
     * @param charset The charset to be associated with the locale
     */
    public void addCharsetMappingFromDeploymentDescriptor(String locale,String charset) {
        map.put( locale, charset );
    }


}
