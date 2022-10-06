//package br.edu.iffar.fw.comendo;
//
//import java.io.IOException;
//
//import javax.faces.component.FacesComponent;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerResponseContext;
//import javax.ws.rs.container.ContainerResponseFilter;
//
//@FacesComponent
//public class CORSFilter implements Filter {
////    static Logger logger = LoggerFactory.getLogger(CORSFilter.class);
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse res,
//            FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "*");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        chain.doFilter(request, response);
//    }
//
//    public void destroy() {
//    }
//}