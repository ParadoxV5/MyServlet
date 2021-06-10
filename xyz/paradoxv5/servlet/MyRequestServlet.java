package xyz.paradoxv5.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  A version of {@link MyServlet} that declares {@code abstract} methods with only
  the {@link HttpServletRequest request} parameter, ignoring the {@link HttpServletResponse response}.
  @see #doGet(HttpServletRequest, HttpServletResponse)
  @see #doPost(HttpServletRequest, HttpServletResponse)
  @version 2021-06-09.0
*/
public abstract class MyRequestServlet extends MyServlet {
  private static final long serialVersionUID = 0;
  
  /** @param request See {@link #doGet(HttpServletRequest, HttpServletResponse)} */
  protected abstract void doGet(HttpServletRequest request);
  /**
    call {@link #doGet(HttpServletRequest) doGet(request)}
    @param request
      the request object
    @param __
      the response object (ignored)
    @see
      MyServlet#doGet(HttpServletRequest, HttpServletResponse)
  */
  @Override protected final void doGet(HttpServletRequest request, HttpServletResponse __) throws ServletException, IOException {
    doGet(request);
  }
  
  /** @param request See {@link #doPost(HttpServletRequest, HttpServletResponse)} */
  protected abstract void doPost(HttpServletRequest request);
  /**
    call {@link #doPost(HttpServletRequest)  doPost(request)}
    @param request
      the request object
    @param __
      the response object (ignored)
    @see
      MyServlet#doPost(HttpServletRequest, HttpServletResponse)
  */
  @Override protected final void doPost(HttpServletRequest request, HttpServletResponse __) throws ServletException, IOException {
    doPost(request);
  }
}