package xyz.paradoxv5.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
  
*/
public class HTTPException extends RuntimeException implements Comparable<HTTPException> {
  private static final long serialVersionUID = 0;
  
  protected final int httpCode;
  public final int getHttpCode() { return httpCode; }
  private static String httpCodeToStringMessage(int httpCode) {
    return '<' + Integer.toString(httpCode) + '>';
  }
  
  protected final String rawMessage;
  public final String getRawMessage() { return rawMessage; }
  
  /**
    
  */
  public HTTPException(int httpCode) {
    super(httpCodeToStringMessage(httpCode));
    this.httpCode = httpCode;
    rawMessage = null;
  }
  /**
  
  */
  public HTTPException(int httpCode, String rawMessage) {
    super(httpCodeToStringMessage(httpCode) + ' ' + rawMessage);
    this.httpCode = (short)httpCode;
    this.rawMessage = rawMessage;
  }
  /**
    
  */
  public HTTPException(int httpCode, Throwable cause) {
    super(httpCodeToStringMessage(httpCode), cause);
    this.httpCode = (short)httpCode;
    rawMessage = null;
  }
  /**
  
  */
  public HTTPException(int httpCode, String rawMessage, Throwable cause) {
    super(httpCodeToStringMessage(httpCode) + ' ' + rawMessage, cause);
    this.httpCode = (short)httpCode;
    this.rawMessage = rawMessage;
  }
  
  public void respond(HttpServletResponse response) throws IOException {
    String httpMessage = getCause().getLocalizedMessage();
    if(rawMessage != null)
      httpMessage = rawMessage + '\n' + httpMessage;
    response.sendError(httpCode, httpMessage);
  }
  
  @Override public int hashCode() {
    return httpCode ^ java.util.Objects.hashCode(rawMessage);
  }
  @Override public int compareTo(HTTPException other) {
    return Integer.compare(httpCode, other.httpCode);
  }
}