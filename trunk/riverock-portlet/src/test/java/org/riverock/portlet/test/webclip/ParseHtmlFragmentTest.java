package org.riverock.portlet.test.webclip;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;

import org.apache.html.dom.HTMLDocumentImpl;
import org.apache.commons.lang.CharEncoding;

import org.cyberneko.html.parsers.DOMFragmentParser;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * User: SMaslyukov
 * Date: 28.03.2007
 * Time: 15:26:04
 */
public class ParseHtmlFragmentTest {

    private static final char SEPARATOR_HTTP_REQUEST_PARAM = '&';

    public static void main(String[] args) throws IOException, SAXException {
        DOMFragmentParser parser = new DOMFragmentParser();

        HTMLDocument document = new HTMLDocumentImpl();
        DocumentFragment fragment;
        fragment = document.createDocumentFragment();

        InputSource inputSource = new InputSource(ParseHtmlFragmentTest.class.getResourceAsStream("/xml/webclip/test.html"));
        inputSource.setEncoding(CharEncoding.UTF_8);
        parser.parse(inputSource, fragment);

        File tempDir = new File(System.getProperty("java.io.tmpdir"));

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        print(fragment, os);

        String s = new String(os.toByteArray(), CharEncoding.UTF_8);
        System.out.println("s = " + s);

        FileOutputStream out = new FileOutputStream(File.createTempFile("parse-test", ".xml", tempDir));
//        out.write(os.toByteArray());
        out.write(s.getBytes(CharEncoding.UTF_8));
        out.flush();
        out.close();
        out = null;
    }

    /**
     * recursively print node
     *
     * @param node
     * @param out output stream
     * @throws IOException
     */
    private static void print(Node node, OutputStream out) throws IOException {
        if (node == null) {
            return;
        }

        int type = node.getNodeType();
        switch (type) {
            case Node.DOCUMENT_NODE:
                print(((Document) node).getDocumentElement(), out);
                out.flush();
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    node = childNodes.item(i);
                    print(node, out);
                    out.flush();
                }
                break;

            case Node.ELEMENT_NODE: {
                    out.write('<');
                    out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                    Attr attrs[] = sortAttributes(node.getAttributes());
                    for (Attr attr : attrs) {
                        out.write(' ');
                        out.write(attr.getNodeName().getBytes(CharEncoding.UTF_8));
                        out.write("=\"".getBytes(CharEncoding.UTF_8));
                        out.write(attr.getNodeValue().getBytes(CharEncoding.UTF_8));
                        out.write('"');
                    }
                    out.write('>');

                NodeList children = node.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        print(children.item(i), out);
                    }
                }
                break;
            }

            // handle entity reference nodes
            case Node.ENTITY_REFERENCE_NODE: {
                out.write(SEPARATOR_HTTP_REQUEST_PARAM);
                out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                out.write(';');
                break;
            }

            // print cdata sections
            case Node.CDATA_SECTION_NODE: {
                out.write("<![CDATA[".getBytes(CharEncoding.UTF_8));
                out.write(node.getNodeValue().getBytes(CharEncoding.UTF_8));
                out.write("]]>".getBytes(CharEncoding.UTF_8));
                break;
            }

            // print text
            case Node.TEXT_NODE: {
                out.write(node.getNodeValue().getBytes(CharEncoding.UTF_8));
                break;
            }

            // print processing instruction
            case Node.PROCESSING_INSTRUCTION_NODE: {
                out.write("<?".getBytes(CharEncoding.UTF_8));
                out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    out.write(' ');
                    out.write(data.getBytes(CharEncoding.UTF_8));
                }
                out.write("?>".getBytes(CharEncoding.UTF_8));
                break;
            }
        }

        if (type == Node.ELEMENT_NODE) {
            out.write("</".getBytes(CharEncoding.UTF_8));
            out.write(node.getNodeName().getBytes(CharEncoding.UTF_8));
            out.write('>');
        }

        out.flush();

    }

    /**
     * сортировка аттрибутов ноды
     *
     * @param attrs мапа с аттрибутами
     * @return Attr[]
     */
    private static Attr[] sortAttributes(NamedNodeMap attrs) {

        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        if (attrs==null) {
            return array;
        }
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return (array);
    }
}
