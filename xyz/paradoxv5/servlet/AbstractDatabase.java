package xyz.paradoxv5.servlet;

import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;
import javax.naming.NamingException;

/**
  TODO: Docs
 * @param <E> currently unused
*/
public abstract class AbstractDatabase<E extends Comparable<E>> {
  protected final DataSource dataSource;
  
  /** TODO: Docs
   * @param resourceName
   * this must match your resource name in context.xml
   * @throws NamingException todo */
  public AbstractDatabase(String resourceName) throws NamingException {
    dataSource = (DataSource)((new javax.naming.InitialContext()).lookup("java:/comp/env/" + resourceName));
  }
  
  public synchronized Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}