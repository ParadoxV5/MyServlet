package xyz.paradoxv5.servlet;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/** ́… */
public abstract class AbstractFilter extends HttpFilter {
  private static final long serialVersionUID = 0;

  /**
    …
    @param request …
    @param response …
    @return …
    @throws IOException …
    @throws ServletException …
  */
  protected abstract boolean filter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
  /**
    …
    @param request …
    @param response …
    @throws IOException …
    @throws ServletException …
  */
  protected void postProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {}
  
  /**
    @implSpec … …
  */
  @Override public final void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;
    
    if(!filter(httpRequest, httpResponse)) return;
    
    try { super.doFilter(httpRequest, httpResponse, chain); }
    // Any exception should be thrown *after* `postFilter`
    finally { postProcess(httpRequest, httpResponse); }
  }
  
  /** … */
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), getFilterName());
  }
}