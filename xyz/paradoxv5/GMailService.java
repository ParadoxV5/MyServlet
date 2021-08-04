package xyz.paradoxv5;

import java.util.Scanner;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

/** GMail Java (Jakarta) Mail service base class
  @version 1
*/
public class GMailService {
  
  /** Reüsable properties for GMail connection */
  public static final java.util.Properties PROPERTIES = new java.util.Properties() { private static final long serialVersionUID = 0; {
    setProperty("mail.transport.protocol", "smtps");
    setProperty("mail.smtp.host", "smtp.gmail.com");
    setProperty("mail.smtp.port", "465");
    setProperty("mail.smtps.auth", "true");
    setProperty("mail.smtps.quitwait", "false");
  } };
  /** Common MIME type, constant for {@link #send(String, String, String, String, String, String)} */
  public static final String TEXT = "text/plain", HTML = "text/html";
  
  /** The sender’s e-mail address, doubling as the “username” for authentication */
  protected final String email;
  /** The sender account’s password */
  protected final String password;
  
  /**
    @param email {@link #email}
    @param password {@link #password}
  */
  public GMailService(String email, String password) {
    this.email = email;
    this.password = password;
  }
  /**
    @param path
      The path to the file
    @return
      {@linkplain #GMailService(String, String) An instance} with the file’s lines as constructor arguments
    @throws FileNotFoundException
      Thrown by {@link Scanner#Scanner(java.io.File)} if no file exists at {@code path}
  */
  public static GMailService fromFile(String path) throws FileNotFoundException {
    try(Scanner scanner = new Scanner(new java.io.File(path))) {
      return new GMailService(scanner.nextLine(), scanner.nextLine());
    }
  }
  
  /**
    {@linkplain Transport#sendMessage(javax.mail.Message, javax.mail.Address[]) Send}
    an
    {@linkplain MimeMessage e-mail};
    {@linkplain Session#getInstance(java.util.Properties) use}
    {@link #PROPERTIES}
    for
    JavaMail API configs
    
    @param from
      The sender’s name
    @param to
      The recipent’s name
    @param toAddress
      The recipent’s e-mail address
    @param subject
      The e-mail’s subject
    @param body
      The e-mail’s body contents
    @param mimeType
      The e-mail’s MIME type (e.g., {@link #TEXT}, {@link #HTML}, …)
    @throws MessagingException
      May be thrown by the JavaMail API if an error occurs
    @throws UnsupportedEncodingException
      Thrown by {@link InternetAddress#InternetAddress(String, String)} if {@code from} or {@code to} is in an unsupported encoding
  */
  public void send(String from, String to, String toAddress, String subject, String body, String mimeType) throws MessagingException, UnsupportedEncodingException {
    Session session = Session.getInstance(PROPERTIES);
    
    MimeMessage message = new MimeMessage(session);
    message.setSubject(subject);
    message.setContent(body, mimeType);
    message.setFrom(new InternetAddress(email, from));
    message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toAddress, to));
    
    try(Transport transport = session.getTransport()) {
      transport.connect(email, password);
      transport.sendMessage(message, message.getAllRecipients());
    }
  }
}