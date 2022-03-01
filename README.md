# SpringBoot-Body-Decryption

### Filter

```java

@Component
public class RequestBodyDecryptFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestWrapper wrappedRequest = new RequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(wrappedRequest, response);
    }

    private class RequestWrapper extends HttpServletRequestWrapper {

        private final byte[] bodyBytes;

        public RequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            bodyBytes = AESUtils.decrypt(new String(StreamUtils.copyToByteArray(request.getInputStream()))).getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new DecryptServletInputStream(bodyBytes);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bodyBytes)));
        }
    }

    private class DecryptServletInputStream extends ServletInputStream {

        private InputStream cachedBodyInputStream;

        private DecryptServletInputStream(byte[] bodyBytes) {
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

#Demo

    curl -XPOST -H "Content-type: application/json" -d '9KMaVQa+bS+smEc5NWtn4Cfkjpa6GT6rhmdMwYHgt3s=' 'http://localhost:8080/hello'

    hello revise