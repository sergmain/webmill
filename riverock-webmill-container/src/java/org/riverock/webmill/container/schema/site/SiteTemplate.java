/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site;





import java.util.ArrayList;
import java.util.Iterator;

/**
 * Both attributes, 'nameTemplate' and 'name' are deprecated. Must
 * not used
 * 
 * @version $Revision$ $Date$
 */
public class SiteTemplate {



     //- Class/Member Variables -/


    /**
     * Field _nameTemplate
     */
    private java.lang.String _nameTemplate;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _role
     */
    private java.lang.String _role;

    /**
     * Field _siteTemplateItemList
     */
    private java.util.ArrayList<TemplateItemType> _siteTemplateItemList;






    public SiteTemplate() 
     {
        super();
        _siteTemplateItemList = new ArrayList<TemplateItemType>();
    } //-- org.riverock.webmill.container.schema.site.SiteTemplate()






    /**
     * Method addSiteTemplateItem
     * 
     * 
     * 
     * @param vSiteTemplateItem
     */
    public void addSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType vSiteTemplateItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _siteTemplateItemList.add(vSiteTemplateItem);
    } //-- void addSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType) 

    /**
     * Method addSiteTemplateItem
     * 
     * 
     * 
     * @param index
     * @param vSiteTemplateItem
     */
    public void addSiteTemplateItem(int index, org.riverock.webmill.container.schema.site.TemplateItemType vSiteTemplateItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _siteTemplateItemList.add(index, vSiteTemplateItem);
    } //-- void addSiteTemplateItem(int, org.riverock.webmill.container.schema.site.TemplateItemType) 

    /**
     * Method clearSiteTemplateItem
     * 
     */
    public void clearSiteTemplateItem()
    {
        _siteTemplateItemList.clear();
    } //-- void clearSiteTemplateItem() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'nameTemplate'.
     * 
     * @return String
     * @return the value of field 'nameTemplate'.
     */
    public java.lang.String getNameTemplate()
    {
        return this._nameTemplate;
    } //-- java.lang.String getNameTemplate() 

    /**
     * Returns the value of field 'role'.
     * 
     * @return String
     * @return the value of field 'role'.
     */
    public java.lang.String getRole()
    {
        return this._role;
    } //-- java.lang.String getRole() 

    /**
     * Method getSiteTemplateItem
     * 
     * 
     * 
     * @param index
     * @return TemplateItemType
     */
    public org.riverock.webmill.container.schema.site.TemplateItemType getSiteTemplateItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _siteTemplateItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.riverock.webmill.container.schema.site.TemplateItemType) _siteTemplateItemList.get(index);
    } //-- org.riverock.webmill.container.schema.site.TemplateItemType getSiteTemplateItem(int) 

    /**
     * Method getSiteTemplateItem
     * 
     * 
     * 
     * @return TemplateItemType
     */
    public org.riverock.webmill.container.schema.site.TemplateItemType[] getSiteTemplateItem()
    {
        int size = _siteTemplateItemList.size();
        org.riverock.webmill.container.schema.site.TemplateItemType[] mArray = new org.riverock.webmill.container.schema.site.TemplateItemType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.riverock.webmill.container.schema.site.TemplateItemType) _siteTemplateItemList.get(index);
        }
        return mArray;
    } //-- org.riverock.webmill.container.schema.site.TemplateItemType[] getSiteTemplateItem() 

    /**
     * Method getSiteTemplateItemAsReference
     * 
     * Returns a reference to 'siteTemplateItem'. No type checking
     * is performed on any modications to the Collection.
     * 
     * @return ArrayList
     * @return returns a reference to the Collection.
     */
    public java.util.ArrayList<TemplateItemType> getSiteTemplateItemAsReference()
    {
        return _siteTemplateItemList;
    } //-- java.util.ArrayList getSiteTemplateItemAsReference() 

    /**
     * Method getSiteTemplateItemCount
     * 
     * 
     * 
     * @return int
     */
    public int getSiteTemplateItemCount()
    {
        return _siteTemplateItemList.size();
    } //-- int getSiteTemplateItemCount() 

    /**
     * Method removeSiteTemplateItem
     * 
     * 
     * 
     * @param vSiteTemplateItem
     * @return boolean
     */
    public boolean removeSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType vSiteTemplateItem)
    {
        boolean removed = _siteTemplateItemList.remove(vSiteTemplateItem);
        return removed;
    } //-- boolean removeSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'nameTemplate'.
     * 
     * @param nameTemplate the value of field 'nameTemplate'.
     */
    public void setNameTemplate(java.lang.String nameTemplate)
    {
        this._nameTemplate = nameTemplate;
    } //-- void setNameTemplate(java.lang.String) 

    /**
     * Sets the value of field 'role'.
     * 
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role)
    {
        this._role = role;
    } //-- void setRole(java.lang.String) 

    /**
     * Method setSiteTemplateItem
     * 
     * 
     * 
     * @param index
     * @param vSiteTemplateItem
     */
    public void setSiteTemplateItem(int index, org.riverock.webmill.container.schema.site.TemplateItemType vSiteTemplateItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _siteTemplateItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _siteTemplateItemList.set(index, vSiteTemplateItem);
    } //-- void setSiteTemplateItem(int, org.riverock.webmill.container.schema.site.TemplateItemType) 

    /**
     * Method setSiteTemplateItem
     * 
     * 
     * 
     * @param siteTemplateItemArray
     */
    public void setSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType[] siteTemplateItemArray)
    {
        //-- copy array
        _siteTemplateItemList.clear();
        for (final TemplateItemType newVar : siteTemplateItemArray) {
            _siteTemplateItemList.add(newVar);
        }
    } //-- void setSiteTemplateItem(org.riverock.webmill.container.schema.site.TemplateItemType) 

    /**
     * Method setSiteTemplateItem
     * 
     * Sets the value of 'siteTemplateItem' by copying the given
     * ArrayList.
     * 
     * @param siteTemplateItemCollection the ArrayList to copy.
     */
    public void setSiteTemplateItem(java.util.ArrayList siteTemplateItemCollection)
    {
        //-- copy collection
        _siteTemplateItemList.clear();
        for (int i = 0; i < siteTemplateItemCollection.size(); i++) {
            _siteTemplateItemList.add((org.riverock.webmill.container.schema.site.TemplateItemType)siteTemplateItemCollection.get(i));
        }
    } //-- void setSiteTemplateItem(java.util.ArrayList) 

    /**
     * Method setSiteTemplateItemAsReference
     * 
     * Sets the value of 'siteTemplateItem' by setting it to the
     * given ArrayList. No type checking is performed.
     * 
     * @param siteTemplateItemCollection the ArrayList to copy.
     */
    public void setSiteTemplateItemAsReference(java.util.ArrayList<TemplateItemType> siteTemplateItemCollection)
    {
        _siteTemplateItemList = siteTemplateItemCollection;
    } //-- void setSiteTemplateItemAsReference(java.util.ArrayList) 

    public String toString() {
        String s = "";

        s +=
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<SiteTemplate";
        if (getRole()!=null) {
            s += " role=\""+getRole()+"\"";
        }
        s += ">\n";

        Iterator<TemplateItemType> items = getSiteTemplateItemAsReference().iterator();
        while (items.hasNext()) {
            TemplateItemType templateItemType = items.next();
            s += "    <SiteTemplateItem  type=\""+templateItemType.getType()+"\" value=\""+templateItemType.getValue()+"\"";
            if (templateItemType.getCode()!=null) {
                s += " code=\""+templateItemType.getCode()+"\"";
            }
            if (templateItemType.getXmlRoot()!=null) {
                s += " xmlRoot=\""+templateItemType.getXmlRoot()+"\"";
            }

            if (templateItemType.getParameterCount()==0) {
                s += "/>\n";
            }
            else {
                s += ">\n";

                Iterator<SiteTemplateParameterType> iterator = templateItemType.getParameterAsReference().iterator();
                while (iterator.hasNext()) {
                    SiteTemplateParameterType siteTemplateParameterType = iterator.next();
                    s += "        <Parameter name=\""+siteTemplateParameterType.getName()+"\" value=\""+siteTemplateParameterType.getValue()+"\"/>\n";
                }
                s += "    </SiteTemplateItem>\n";

            }
        }

        s += "</SiteTemplate>";

        return s;
    }
}
