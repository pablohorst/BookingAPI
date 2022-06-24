package com.pablohorst.booking.api.logging;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author Pablo Horst
 */
public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
    private ByteArrayOutputStream cachedStream;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedStream == null) {
            cacheInputStream();
        }

        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        cachedStream = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedStream);
    }

    public class CachedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream input;

        public CachedServletInputStream() {
            input = new ByteArrayInputStream(cachedStream.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return input.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            // Not doing anything with the read listener
        }

        @Override
        public int read() {
            return input.read();
        }
    }
}
