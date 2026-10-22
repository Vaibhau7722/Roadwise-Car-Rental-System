package com.carrental.filter;

import com.carrental.model.UserSession;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityFilter implements Filter {
    public static final String USER="authenticatedUser", CSRF="csrfToken";
    private final SecureRandom random=new SecureRandom();
    @Override public void doFilter(ServletRequest rawRequest,ServletResponse rawResponse,FilterChain chain)throws IOException,ServletException {
        HttpServletRequest request=(HttpServletRequest)rawRequest;HttpServletResponse response=(HttpServletResponse)rawResponse;HttpSession session=request.getSession(true);
        if(session.getAttribute(CSRF)==null){byte[] bytes=new byte[32];random.nextBytes(bytes);session.setAttribute(CSRF,Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));}
        request.setAttribute(CSRF,session.getAttribute(CSRF));Object notice=session.getAttribute("notice");if(notice!=null){request.setAttribute("notice",notice);session.removeAttribute("notice");}
        String path=request.getRequestURI().substring(request.getContextPath().length());Object principal=session.getAttribute(USER);
        if(path.startsWith("/admin") && (!(principal instanceof UserSession u)||!"ADMIN".equals(u.role()))){if(principal==null)response.sendRedirect(request.getContextPath()+"/login");else response.sendError(HttpServletResponse.SC_FORBIDDEN,"Administrator access is required.");return;}
        if(path.startsWith("/customer") && (!(principal instanceof UserSession u)||!"CUSTOMER".equals(u.role()))){if(principal==null)response.sendRedirect(request.getContextPath()+"/login");else response.sendError(HttpServletResponse.SC_FORBIDDEN,"Customer access is required.");return;}
        if("POST".equalsIgnoreCase(request.getMethod())){String expected=(String)session.getAttribute(CSRF),provided=request.getParameter("_csrf");if(expected==null||provided==null||!MessageDigest.isEqual(expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),provided.getBytes(java.nio.charset.StandardCharsets.UTF_8))){response.sendError(HttpServletResponse.SC_FORBIDDEN,"Invalid or expired form token. Reload the page and try again.");return;}}
        chain.doFilter(request,response);
    }
}
