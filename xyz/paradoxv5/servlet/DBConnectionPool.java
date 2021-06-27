package xyz.paradoxv5.servlet;

import javax.sql.DataSource;
import java.sql.Connection;

import java.sql.SQLException;
import javax.naming.NamingException;

/**
  TODO: Docs
*/
public class DBConnectionPool {
  protected final DataSource dataSource;
  
  /** TODO: Docs
   * @param resourceName
   * this must match your resource name in context.xml
   * @throws NamingException todo */
  public DBConnectionPool(String resourceName) throws NamingException {
    dataSource = (DataSource)((new javax.naming.InitialContext()).lookup("java:/comp/env/" + resourceName));
  }
  
  public synchronized Connection get() throws SQLException {
    return dataSource.getConnection();
  }
}