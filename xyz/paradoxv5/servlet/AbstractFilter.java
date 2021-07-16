package xyz.paradoxv5.servlet;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/** … */
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
  @Override public final void doFilter(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain chain) throws IOException, ServletException {
    if(!filter(request, response)) return;
    // Any exception should be thrown *after* `postProcess`
    try { super.doFilter(request, response, chain); }
    finally { postProcess(request, response); }
  }
  
  /** … */
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), getFilterName());
  }
}