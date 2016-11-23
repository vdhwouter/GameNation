package GameNationBackEnd.Filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RoutingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String uri = req.getRequestURI();

        if (uri.startsWith("/api") || uri.startsWith("/oauth") || uri.endsWith(".html") || uri.endsWith(".css") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".ttf") || uri.endsWith(".js")) {
            chain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

    @Override
    public void destroy() {}
}
