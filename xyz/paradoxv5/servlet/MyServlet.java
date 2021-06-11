package xyz.paradoxv5.servlet;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Javadoc imports used by Java
import javax.servlet.http.HttpServlet;
import java.util.function.Supplier;
// Javadoc imports not used by Java
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
  An abstract {@link HttpServlet} class I use to collect up some
  repeated code I commonly run into while taking this Web Dev tutorial class.
  <p>
  This class also marks {@link #doGet(HttpServletRequest, HttpServletResponse)} and
  {@link #doPost(HttpServletRequest, HttpServletResponse)} {@code abstract} for its children.
  
  @version 2021-06-11.0
  @author ParadoxV5
*/
public abstract class MyServlet extends HttpServlet implements java.util.EventListener {
  private static final long serialVersionUID = 0;
  
  /**
    This method is equivalent to a backport of <a href="
      https://docs.oracle.com/javase/9/docs/api/java/util/Objects.html#requireNonNullElseGet-T-java.util.function.Supplier-
    ">{@code requireNonNullElseGet(T, }{@link Supplier}{@code )}
    that’s added in Java 9</a>.
    <br>
    (I still had to use Java 8 for my {@link HttpServlet Servlet}s. Maybe because NetBeans still use Java 8?)
    
    @param obj
      the object to test
    @param supplier
      the {@link Supplier} that this uses if {@code obj} is {@code null}
    @return
      {@code obj} if it’s not {@code null}, {@link Supplier#get() supplier.get()} if it is
  */
  public static <T> T nonNullElseGet​(T obj, Supplier<? extends T> supplier) {
    return (obj == null) ? supplier.get() : obj;
  }
  
  /**
    Get the session using 
    @param request
      The {@link HttpServletRequest} to query the session from
    @param fresh
      if {@code true}, {@link HttpSession#invalidate()} (reset) the current
      session (if there is one) so the queried will be a new one
    @return
      {@link HttpServletRequest#getSession()}
  */
  protected static HttpSession getSession(HttpServletRequest request, boolean fresh) {
    if(fresh) {
      HttpSession oldSession = request.getSession(false);
      if(oldSession != null) oldSession.invalidate();
    }
    return request.getSession();
  }
  
  /**
    Forward the {@code request} and {@code response} to the resource at the given {@code path}
    @param path
      See {@link ServletContext#getRequestDispatcher(String)}
    @param request
      See {@link RequestDispatcher#forward(ServletRequest, ServletResponse)}
    @param response ditto
    @throws ServletException ditto
    @throws IOException ditto
  */
  protected void forward(String path, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    getServletContext().getRequestDispatcher(path).forward(request, response);
  }
  
  @Override protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
  @Override protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}