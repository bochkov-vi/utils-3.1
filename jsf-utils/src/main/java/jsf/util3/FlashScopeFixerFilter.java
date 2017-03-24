package jsf.util3;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Created by bochkov on 24.03.17.
 */
public class FlashScopeFixerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Below line: response.getWriter() must be invoked to buffer size setting work. Just DO NOT touch this!
        response.getWriter();
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
        wrapper.setBufferSize(10000000);
        chain.doFilter(request, wrapper);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
    @Override
    public void destroy() {}
}