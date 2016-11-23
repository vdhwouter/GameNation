package GameNationBackEnd.Filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class RoutingFilter implements Filter {

    String[] routeStartWith = { "/api", "/oauth", "/logout" };
    String[] routeEndsWith = { ".html", ".css", ".jpg", ".png", ".ttf", ".js"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String uri = req.getRequestURI();

        if (Arrays.stream(routeStartWith).anyMatch(start -> uri.startsWith(start)) || Arrays.stream(routeEndsWith).anyMatch(start -> uri.endsWith(start))) {
            chain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/index.html").forward(request, response);
        }
    }

    @Override
    public void destroy() {}
}
