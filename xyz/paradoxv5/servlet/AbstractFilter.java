package xyz.paradoxv5.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

public abstract class AbstractFilter implements Filter {
  /** … */
  private FilterConfig filterConfig;
  /** @return {@link #filterConfig} */
  protected FilterConfig getFilterConfig() { return filterConfig; }
  /**
    @param filterConfig …
  */
  @Override public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }
  
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
  protected void postFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {}
  
  /**
    @implSpec … …
  */
  @Override public final void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;
    
    filter(httpRequest, httpResponse);
    
    try { chain.doFilter(httpRequest, httpResponse); }
    // Any exception should be thrown *after* `postFilter`
    finally { postFilter(httpRequest, httpResponse); }
  }
  
  /** … */
  @Override public String toString() {
    return String.format("%s<%s>]", getClass().getSimpleName(), getFilterConfig().getFilterName());
  }
}