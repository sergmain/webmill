package org.riverock.webmill.portlet.wrapper;

import java.io.PrintWriter;
import java.io.Writer;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.riverock.generic.tools.servlet.ServletOutputStreamWithWriter;
import org.riverock.generic.tools.servlet.ServletOutputStreamWithOutputStream;

public final class ServletResponseWrapperInclude extends HttpServletResponseWrapper {

    private Writer writer= null;
    private OutputStream outputStream = null;
    boolean isWriter = true;

    protected void finalize() throws Throwable {
        writer = null;
        super.finalize();
    }

    public ServletResponseWrapperInclude( ServletResponse response, Writer writer) {
        super( (HttpServletResponse)response );
        this.writer = writer;
    }

    public ServletResponseWrapperInclude( ServletResponse response, OutputStream outputStream) {
        super( (HttpServletResponse)response );
        this.outputStream = outputStream;
        isWriter = false;
    }

    public java.io.PrintWriter getWriter() throws java.io.IOException {
        if (isWriter)
            return new PrintWriter( writer );
        else
            return new PrintWriter( outputStream );
    }

    public ServletOutputStream getOutputStream() throws java.io.IOException {
        if (isWriter)
            return new ServletOutputStreamWithWriter( writer );
        else
            return new ServletOutputStreamWithOutputStream(outputStream);
    }

    //Todo: uncomment and implement
/*
    public int getBufferSize() {
        return response.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        response.flushBuffer();
    }

    public void resetBuffer() {
        response.resetBuffer();
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    public void reset() {
        response.reset();
    }
*/
}
