# SpringBoot-Body-Decryption

### Filter

```java

package com.tirmizee.filter;

import com.tirmizee.utils.AESUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class RequestBodyDecryptFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) request);
        wrappedRequest.decrypt();
        filterChain.doFilter(wrappedRequest, response);
    }

    private class RequestWrapper extends HttpServletRequestWrapper {

        private byte[] bodyBytes;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
        }

        public void decrypt() {
            bodyBytes = AESUtils.decrypt(new String(bodyBytes)).getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStreamWrapper(bodyBytes);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bodyBytes)));
        }
    }

    private class ServletInputStreamWrapper extends ServletInputStream {

        private InputStream cachedBodyInputStream;

        private ServletInputStreamWrapper(byte[] bodyBytes) {
            this.cachedBodyInputStream = new ByteArrayInputStream(bodyBytes);
        }

        public boolean isFinished() {
            try {
                return cachedBodyInputStream.available() == 0;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {

        }

        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }

}


```

### Demo

    curl -XPOST -H "Content-type: application/json" -d '9KMaVQa+bS+smEc5NWtn4Cfkjpa6GT6rhmdMwYHgt3s=' 'http://localhost:8080/hello'

    hello revise