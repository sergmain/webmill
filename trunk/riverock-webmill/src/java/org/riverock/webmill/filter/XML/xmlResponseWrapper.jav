package com.jsmithy.filter.XML;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

// Referenced classes of package com.cj.xmlflt:
//            FilterServletOutputStream

public class xmlResponseWrapper extends HttpServletResponseWrapper
{

    private ByteArrayOutputStream output;
    private int contentLength;
    private String contentType;

    public xmlResponseWrapper(HttpServletResponse httpservletresponse)
    {
        super(httpservletresponse);
        output = new ByteArrayOutputStream();
    }

    public int getContentLength()
    {
        return contentLength;
    }

    public String getContentType()
    {
        return contentType;
    }

    public byte[] getData()
    {
        return output.toByteArray();
    }

    public ServletOutputStream getOutputStream()
    {
        return new FilterServletOutputStream(output);
    }

    public PrintWriter getWriter()
    {
        return new PrintWriter(getOutputStream(), true);
    }

    public void setContentLength(int i)
    {
        contentLength = i;
        super.setContentLength(i);
    }

    public void setContentType(String s)
    {
        contentType = s;
        super.setContentType(s);
    }
}
