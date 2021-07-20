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
  
  /**
    Log to the given {@code logger} with the {@link java.util.logging.Level logging level}
    determined by the category (the code truncate the last two digits) of the {@link #httpCode}: <ul>
      <li>5xx Server errors: Severe
      <li>4xx Client errors: Warning
      <li>2xx Informational responses: Info
      <li>1xx Successful responses & 3xx Redirects: Config
      <li>others (Not a standardized HTTP category): Fine
    </ul>
    
    @param logger
      the Logger
  */
  public void log(java.util.logging.Logger logger) {
    switch(httpCode / 100) {
      case 5: logger.severe(getMessage()); break;
      case 4: logger.warning(getMessage()); break;
      case 2: logger.info(getMessage()); break; // 
      case 1: // 
      case 3: // 
        logger.config(getMessage());
      break;
      default: // 
        logger.fine(getMessage());
    }
  }
  /** {@link #log(java.util.logging.Logger) Log} to {@link AbstractServlet#LOGGER} */
  public void log() { log(AbstractServlet.LOGGER); }
  
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