package xyz.paradoxv5.servlet;

import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
  This is a runtime exception class based on an HTTP response code ({@link #httpCode}).
  To wrap an underlying exception, utilize the {@link #getCause() cause} constructor
    parameter or configure it with {@link Exception#initCause(Throwable)}
  
  @version
    1.01
  @apiNote
    The {@linkplain #getMessage() error message} of instances, initialized via {@code super}
    constructor calls, is “{@link #httpCodeToStringMessage(int) &lt;httpCode>}”, or if an additional
    {@link #originalMessage message} is supplied in the constructor call, it followed by “{@link #originalMessage  originalMessage}”
  
  @see #HTTPException(int)
  @see #HTTPException(int, String)
  @see #HTTPException(int, Throwable)
  @see #HTTPException(int, String, Throwable)
*/
public class HTTPException extends RuntimeException implements Comparable<HTTPException> {
  private static final long serialVersionUID = 0;
  
  /** The HTTP response code (not neccessarily valid)
    @see #getHttpCode()
    @see HTTPException
  */
  protected final int httpCode;
  /** @return {@link #httpCode} */
  public final int getHttpCode() { return httpCode; }
  /**
    Central method called by various constructors to convert the {@link #httpCode} parameter
    to the {@link #getMessage() String message} argument of {@code super} constructor calls
    
    @param httpCode
      An {@code int}, typically the {@link #httpCode}
    @return
      {@code <httpCode>} (the argument surrounded by angle-brackets)
  */
  private static String httpCodeToStringMessage(int httpCode) {
    return '<' + Integer.toString(httpCode) + '>';
  }
  
  /** The original message fed in to the constructor
    @see #getOriginalMessage()
    @see HTTPException
    @see #httpCodeToStringMessage(int)
    @see #getMessage()
  */
  protected final String originalMessage;
  public final String getOriginalMessage() { return originalMessage; }
  
  /**
    Constructor with {@code null} {@link #originalMessage} and no
    {@link #getCause() cause} {@link #initCause(Throwable) initialized}
    
    @param httpCode
      {@link #httpCode}
    @see HTTPException
  */
  public HTTPException(int httpCode) {
    super(httpCodeToStringMessage(httpCode));
    this.httpCode = httpCode;
    originalMessage = null;
  }
  /** Constructor with no {@link #getCause() cause} {@link #initCause(Throwable) initialized}
    @param httpCode
      {@link #httpCode}
    @param originalMessage
     {@link #originalMessage}
    @see HTTPException
  */
  public HTTPException(int httpCode, String originalMessage) {
    super(httpCodeToStringMessage(httpCode) + ' ' + originalMessage);
    this.httpCode = httpCode;
    this.originalMessage = originalMessage;
  }
  /** Constructor with {@code null} {@link #originalMessage}
    @param httpCode
      {@link #httpCode}
    @param cause
      the {@link #getCause() cause} to {@link #initCause(Throwable) initialize}
    @see HTTPException
  */
  public HTTPException(int httpCode, Throwable cause) {
    super(httpCodeToStringMessage(httpCode), cause);
    this.httpCode = httpCode;
    originalMessage = null;
  }
  /** Complete constructor
    @param httpCode
      {@link #httpCode}
    @param originalMessage
      {@link #originalMessage}
    @param cause
      the {@link #getCause() cause} to {@link #initCause(Throwable) initialize}
    @see HTTPException
  */
  public HTTPException(int httpCode, String originalMessage, Throwable cause) {
    super(httpCodeToStringMessage(httpCode) + ' ' + originalMessage, cause);
    this.httpCode = httpCode;
    this.originalMessage = originalMessage;
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
  
  /**
    {@linkplain HttpServletResponse#sendError(int, String) Respond an “error”}
    (regardless of {@link #httpCode} category) to the given {@code response};
    The {@code sc} is (of course) {@link #httpCode} and the {@code msg} is, if any,
    {@link #originalMessage} and the {@link #getCause()}’s
    {@link Throwable#getLocalizedMessage() getLocalizedMessage()} each on their own line.
    
    @param response
      the {@link HttpServletResponse} to send the error to
    @throws IOException
      If {@link HttpServletResponse#sendError(int, String)}
      {@code throw}ed an {@code IOException}
  */
  public void respond(HttpServletResponse response) throws IOException {
    Throwable cause = getCause();
    if(cause != null) {
      String causeMessage = cause.getLocalizedMessage();
      if(causeMessage != null) {
        if(originalMessage == null) {
          response.sendError(httpCode, causeMessage);
        } else {
          response.sendError(httpCode, originalMessage + '\n' + causeMessage);
        }
        return;
      }
    }
    if(originalMessage == null) {
      response.sendError(httpCode);
    } else {
      response.sendError(httpCode, originalMessage);
    }
  }
  
  /** @return Bitwise XOR of {@link #httpCode} and {@link #originalMessage}’s {@link Objects#hashCode} */
  @Override public int hashCode() {
    return httpCode ^ Objects.hashCode(originalMessage);
  }
  /** @return a {@link Integer#compare(int, int)} of {@code this} and {@code other}’s {@link #httpCode}s */
  @Override public int compareTo(HTTPException other) {
    return Integer.compare(httpCode, other.httpCode);
  }
}