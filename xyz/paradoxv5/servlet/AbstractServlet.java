package xyz.paradoxv5.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;
import java.io.IOException;
import javax.servlet.ServletException;

// Javadoc imports not used by Java code
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
  My abstract {@link HttpServlet} class and the progenitor of the overall collection
  <p>
  This class also marks {@link #doGet(HttpServletRequest, HttpServletResponse)} and
  {@link #doPost(HttpServletRequest, HttpServletResponse)} {@code abstract} for its children.
  
  @version 3
*/
public abstract class AbstractServlet extends HttpServlet implements java.util.EventListener {
  private static final long serialVersionUID = 2;
  
  /** {@link Logger#getLogger(String) Logger of the name} {@code xyz.paradoxv5.servlet} (the package name, hardcoded) */
  public static final Logger LOGGER = Logger.getLogger("xyz.paradoxv5.servlet");
  
  /**
    This invalidate the current {@link HttpSession session} if there is one.
    <br>
    This method is a convenience over having to null-check if there do, in fact, exists a session currently.
    @param request
      The {@link HttpServletRequest} to query the current session from
  */
  protected static void invalidateSession(HttpServletRequest request) {
    HttpSession oldSession = request.getSession(false);
    if(oldSession != null) oldSession.invalidate();
  }
  
  @Override protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
  @Override protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
  
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
}