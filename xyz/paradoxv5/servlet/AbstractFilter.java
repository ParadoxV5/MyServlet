package xyz.paradoxv5.servlet;

import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

/**
  A custom {@link HttpFilter} class that splits
  {@link #doFilter(HttpServletRequest, HttpServletResponse, FilterChain) doFilter}
  to a {@linkplain #filter(HttpServletRequest, HttpServletResponse) before chaining part}
  and an {@linkplain #postProcess(HttpServletRequest, HttpServletResponse) after chaining part}.
  
  @version 1
*/
public abstract class AbstractFilter extends HttpFilter {
  private static final long serialVersionUID = 0;
  
  /**
    Called by
    {@link #doFilter(HttpServletRequest, HttpServletResponse, FilterChain)}
    <em>before</em> those down the {@link FilterChain} have ended to process any {@code request}-related filtering
    
    @param request
      the HTTP request
    @param response
      the HTTP response
    @return
      {@code true} if the {@code request} passes the filter and should proceed to those down the {@link FilterChain};
      {@code false} if this filter blocks the request and generates a response, and {@code doFilter} should not pass the request & respnse through.
    @throws IOException
      if there’s an unrecoverable exception with IO
    @throws ServletException
      if there’s a unrecoverable Servlet exception in general
  */
  protected abstract boolean filter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
  /**
    Called by
    {@link #doFilter(HttpServletRequest, HttpServletResponse, FilterChain)}
    <em>after</em> those down the {@link FilterChain} have ended to process any {@code response}-related filtering
    
    @param request
      the HTTP request
    @param response
      the HTTP response
    @throws IOException
      if there’s an unrecoverable exception with IO
    @throws ServletException
      if there’s a unrecoverable Servlet exception in general
  */
  protected void postProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {}
  
  /**
    @apiNote <ol>
      <li>
        Call {@link #filter(HttpServletRequest, HttpServletResponse)}, {@code return} if it returns {@code false}
        (or re{@code throw} any of its exceptions)
      <li>
        {@code try}
        {@link HttpFilter#doFilter(HttpServletRequest, HttpServletResponse, FilterChain) super},
        whose default implementation simply passes the request&response down to the next in the {@code FilterChain}
      <li>
        {@code finally} {@link #postProcess(HttpServletRequest, HttpServletResponse)}
      <li>
        Re{@code throw}s any exceptions caught by the {@code try}-{@code finally} blocks (after those blocks)
    </ol>
  */
  @Override public final void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    if(!filter(request, response)) return;
    // Any exception should be thrown *after* `postProcess`
    try { super.doFilter(request, response, chain); }
    finally { postProcess(request, response); }
  }
  
  /** @return
    A {@link String} representation of this filter using its name:
    {@link Class#getSimpleName() getClass().getSimpleName()}{@code <}@{@link #getFilterName()}{@code >}
  */
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), getFilterName());
  }
}