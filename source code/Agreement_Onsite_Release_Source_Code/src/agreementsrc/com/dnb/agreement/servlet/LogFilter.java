package com.dnb.agreement.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogFilter implements Filter {

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    
    log.debug("request "+request.getRequestURL());
    
    Enumeration enumeration = request.getParameterNames();
    while (enumeration.hasMoreElements()) {
      String paramName = (String) enumeration.nextElement();
      log.debug("request parameter ["+paramName+"]="+request.getParameter(paramName));
    }

    HttpSession session = request.getSession();
    enumeration = session.getAttributeNames();
    while (enumeration.hasMoreElements()) {
      String attributeName = (String) enumeration.nextElement();
     // log.debug("session parameter ["+attributeName+"]="+session.getAttribute(attributeName));
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void destroy() {
  }
  
  private static final Log log = LogFactory.getLog(LogFilter.class);
}
