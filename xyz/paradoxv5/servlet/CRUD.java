package xyz.paradoxv5.servlet;

import java.util.Set;

import java.io.Serializable;

/**
  
*/
public interface CRUD<E extends Serializable, K> {
  public E get(K primaryKey);
  public Set<E> getAll() throws Exception;
  
  public void add(E object) throws Exception;
  public void update(E object) throws Exception;
  public void remove(E object) throws Exception;
  
  public static <E extends Serializable> Set<E> throwUnsupportedOperationException() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
}