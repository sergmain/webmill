package com.jsmithy.filter.XML;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.xalan.xslt.*;
import org.apache.xerces.framework.XMLParser;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

// Referenced classes of package com.cj.xmlflt:
//            xmlResponseWrapper

public class XMLFilter
    implements Filter
{

    private FilterConfig config;
    private static final String VERSION = "ver. 1.1";
    private static final String CPR = "(c) Coldjava. mailto:info@servletsuite.com";
    private static Hashtable cnf = new Hashtable();
    private static final int BUFFER_SIZE = 4096;
    private static boolean no_init = true;
    private static final String CONFIG = "config";
    private static final String XMLSOURCE = "xmlsource";
    private static final String XSLSOURCE = "xslsource";
    private static final String RULE = "rule";
    private static final String DEFAULT = "default";
    private static final String AGENT = "agent";
    private static final String ACCEPT = "accept";
    private static final String XML = "xml";
    private static final String XSL = "xsl";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private static final String XMLPREFIX = "xml_";
    private static final String XSLPREFIX = "xsl_";

    public XMLFilter()
    {
    }

    private String checkDocument(Document document)
    {
        NodeList nodelist = document.getDocumentElement().getChildNodes();
        Vector vector = new Vector();
        int k;
        if((k = nodelist.getLength()) < 4)
            return "Wrong configuration file";
        for(int i = 0; i < k; i++)
        {
            Node node = nodelist.item(i);
            if(node.getNodeType() == 1)
            {
                if(node == null)
                    return "Could not get node " + i + " from XML document";
                String s = checkNode(node, vector);
                if(s.length() != 0)
                    return "Node number:" + i + ". " + s;
            }
        }

        String as[] = (String[])cnf.get("default");
        if(as == null)
            return "Could not get default rule";
        if(cnf.get("xsl_" + as[0]) == null)
            return "Could not get xsl source " + as[1];
        for(int j = 0; j < vector.size(); j++)
        {
            String as1[] = (String[])vector.elementAt(j);
            if(cnf.get("xsl_" + as1[0]) == null)
                return "Could not get xsl source " + as1[1];
        }

        cnf.put("rule", vector);
        return "";
    }

    private String checkNode(Node node, Vector vector)
    {
        String s = node.getNodeName();
        NamedNodeMap namednodemap = node.getAttributes();
        if(namednodemap == null)
            return "Could not get attributes";
        if(namednodemap.getLength() == 0)
            return "Could not get attributes";
        if(s.equals("xslsource"))
        {
            Node node1;
            if((node1 = namednodemap.getNamedItem("name")) == null)
                return "Could not get XSL source name";
            String s1;
            if((s1 = node.getFirstChild().getNodeValue()) == null)
                return "Coult not URI for XSL source";
            String s2;
            if((s2 = node1.getNodeValue()) == null)
                return "Could not get XSL source name";
            if(s2.length() == 0)
                return "Could not get XSL source name";
            if(s1.length() == 0)
                return "Coult not URI for XSL source";
            String as[] = {
                s2, s1
            };
            cnf.put("xsl_" + s2, as);
        } else
        if(s.equals("rule"))
        {
            Node node2;
            if((node2 = namednodemap.getNamedItem("xsl")) == null)
                return "Could not get XSL source name for rule";
            Node node4;
            if((node4 = namednodemap.getNamedItem("type")) == null)
                return "Could not get type for rule";
            Node node6;
            if((node6 = namednodemap.getNamedItem("accept")) == null && (node6 = namednodemap.getNamedItem("agent")) == null)
                return "Could not get condition for rule";
            String s7;
            if((s7 = node6.getNodeValue()) == null)
                return "Could not get condition for rule";
            if(s7.length() == 0)
                return "Could not get condition for rule";
            String s3;
            if((s3 = node2.getNodeValue()) == null)
                return "Could not get XSL source name for rule";
            if(s3.length() == 0)
                return "Could not get XSL source name for rule";
            String s5;
            if((s5 = node4.getNodeValue()) == null)
                return "Could not get type for rule";
            if(s5.length() == 0)
                return "Could not get type for rule";
            String as1[] = {
                s3, s5, node6.getNodeName(), s7
            };
            vector.addElement(as1);
        } else
        if(s.equals("default"))
        {
            Node node3;
            if((node3 = namednodemap.getNamedItem("xsl")) == null)
                return "Could not get XSL source name for default rule";
            Node node5;
            if((node5 = namednodemap.getNamedItem("type")) == null)
                return "Could not get type for default rule";
            String s4;
            if((s4 = node3.getNodeValue()) == null)
                return "Could not get XSL source name for default rule";
            if(s4.length() == 0)
                return "Could not get XSL source name for default rule";
            String s6;
            if((s6 = node5.getNodeValue()) == null)
                return "Could not get type for default rule";
            if(s6.length() == 0)
                return "Could not get type for default rule";
            String as2[] = {
                s4, s6
            };
            cnf.put("default", as2);
        } else
        {
            return "Invalid node";
        }
        return "";
    }

    public void destroy()
    {
        config = null;
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
        throws IOException, ServletException
    {
        if(cnf == null)
        {
            filterchain.doFilter(servletrequest, servletresponse);
        } else
        {
            xmlResponseWrapper xmlresponsewrapper = new xmlResponseWrapper((HttpServletResponse)servletresponse);
            filterchain.doFilter(servletrequest, xmlresponsewrapper);
            String s;
            if((s = xmlresponsewrapper.getContentType()) != null)
                if(s.indexOf("xml") >= 0)
                {
                    String s1 = new String(xmlresponsewrapper.getData());
                    processRequest((HttpServletRequest)servletrequest, s1, (HttpServletResponse)servletresponse);
                } else
                {
                    servletresponse.setContentType(s);
                    javax.servlet.ServletOutputStream servletoutputstream = servletresponse.getOutputStream();
                    servletoutputstream.write(xmlresponsewrapper.getData());
                    servletoutputstream.close();
                }
        }
    }

    private void doXSLT(String s, String s1, String s2, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException
    {
        XSLTInputSource xsltinputsource = null;
        XSLTInputSource xsltinputsource1 = null;
        XSLTResultTarget xsltresulttarget = null;
        Object obj = null;
        String as[] = (String[])cnf.get("xsl_" + s1);
        xsltinputsource1 = new XSLTInputSource(new StringReader(s));
        xsltinputsource = new XSLTInputSource(as[1]);
        httpservletresponse.setContentType(s2);
        PrintWriter printwriter = httpservletresponse.getWriter();
        xsltresulttarget = new XSLTResultTarget(printwriter);
/*
      TransformerFactory tFactory = TransformerFactory.newInstance();
      // Get the XML input document and the stylesheet.
      Source xmlSource = new StreamSource(new URL("file:todo.xml").openStream());
      Source xslSource = new StreamSource(new URL("file:todo.xsl").openStream());
      // Generate the transformer.
      Transformer transformer = tFactory.newTransformer(xslSource);
      // Perform the transformation, sending the output to the response.
      transformer.transform(xmlSource, new StreamResult(out));
*/
        try
        {
            XSLTProcessor xsltprocessor = XSLTProcessorFactory.getProcessor();
            xsltprocessor.process(xsltinputsource1, xsltinputsource, xsltresulttarget);
        }
        catch(SAXException saxexception)
        {
            printwriter.println(saxexception.toString());
        }
    }

    private void dumpFile(String s, String s1, ServletResponse servletresponse)
        throws IOException
    {
        byte abyte0[] = new byte[4096];
        servletresponse.setContentType(s1);
        javax.servlet.ServletOutputStream servletoutputstream = servletresponse.getOutputStream();
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s);
            int i;
            while((i = fileinputstream.read(abyte0)) != -1) 
                servletoutputstream.write(abyte0, 0, i);
            fileinputstream.close();
        }
        catch(Exception _ex) { }
        servletoutputstream.close();
    }

    public FilterConfig getFilterConfig()
    {
        return config;
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
        config = filterconfig;
        no_init = false;
        String s = filterconfig.getInitParameter("config");
        System.out.println("XML filter (c) Coldjava. mailto:info@servletsuite.com ver. 1.1");
        if(s == null)
        {
            System.out.println("Could not get initial parameter config");
        } else
        {
            String s1 = readConfig(s);
            if(s1.length() != 0)
            {
                System.out.println(s1);
                cnf = null;
            }
        }
    }

    private void processRequest(HttpServletRequest httpservletrequest, String s, HttpServletResponse httpservletresponse)
        throws IOException
    {
        Vector vector = (Vector)cnf.get("rule");
        String as[] = new String[0];
        for(int i = 0; i < vector.size(); i++)
        {
            as = (String[])vector.elementAt(i);
            String s1 = as[0];
            String s3 = as[1];
            String s5 = as[2];
            String s6 = as[3];
            if(s5.equals("agent"))
                s5 = httpservletrequest.getHeader("User-Agent");
            else
                s5 = httpservletrequest.getHeader("Accept");
            if(s5.indexOf(s6) >= 0)
            {
                doXSLT(s, s1, s3, httpservletrequest, httpservletresponse);
                return;
            }
        }

        as = (String[])cnf.get("default");
        String s2 = as[0];
        String s4 = as[1];
        doXSLT(s, s2, s4, httpservletrequest, httpservletresponse);
    }

    private String readConfig(String s)
    {
        DOMParser domparser = new DOMParser();
        String s1 = "";
        Document document;
        try
        {
            domparser.parse(s);
            document = domparser.getDocument();
        }
        catch(SAXParseException saxparseexception)
        {
            s1 = s1 + "Parse Error.  Line: " + saxparseexception.getLineNumber();
            s1 = s1 + ",URI: " + saxparseexception.getSystemId();
            s1 = s1 + ",Message: " + saxparseexception.getMessage();
            return s1;
        }
        catch(SAXException saxexception)
        {
            s1 = s1 + "SAXException: " + saxexception.getMessage();
            return s1;
        }
        catch(IOException ioexception)
        {
            s1 = s1 + "IO Error: " + ioexception.getMessage();
            return s1;
        }
        return checkDocument(document);
    }

    public void setFilterConfig(FilterConfig filterconfig)
    {
        if(no_init)
        {
            no_init = false;
            config = filterconfig;
            String s = filterconfig.getInitParameter("config");
            System.out.println("XML filter (c) Coldjava. mailto:info@servletsuite.com ver. 1.1");
            if(s == null)
            {
                System.out.println("Could not get initial parameter config");
            } else
            {
                String s1 = readConfig(s);
                if(s1.length() != 0)
                {
                    System.out.println(s1);
                    cnf = null;
                }
            }
        }
    }

}
