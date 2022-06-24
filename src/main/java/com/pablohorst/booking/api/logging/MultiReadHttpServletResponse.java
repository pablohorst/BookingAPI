package com.pablohorst.booking.api.logging;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Pablo Horst
 */
public class MultiReadHttpServletResponse extends HttpServletResponseWrapper {

    private CachedServletOutputStream cachedServletStream;
    private ByteArrayOutputStream cachedStream;

    public MultiReadHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    public String getContent() {
        if (cachedStream == null) {
            return null;
        }
        return cachedStream.toString();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(getOutputStream());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (cachedServletStream == null) {
            cacheOutputStream();
        }
        return cachedServletStream;
    }

    private void cacheOutputStream() throws IOException {
        cachedStream = new ByteArrayOutputStream();
        cachedServletStream = new CachedServletOutputStream(super.getOutputStream(), cachedStream);
    }

    public class CachedServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public CachedServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // Not of any use for now
        }
    }
}
