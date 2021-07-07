package xyz.paradoxv5.servlet.jpa;

import java.util.Objects;

import java.io.Serializable;

/**
  
*/
public abstract class AbstractEntity<K extends Serializable> implements Serializable {
  private static final long serialVersionUID = 0;
  
  protected K primaryKey;
  public AbstractEntity(K primaryKey) {
    this.primaryKey = primaryKey;
  }
  
  @Override public boolean equals(Object obj) {
    return this == obj || (
      obj instanceof AbstractEntity &&
      Objects.equals(primaryKey,  ((AbstractEntity<?>)obj).primaryKey)
    );
  }
  @Override public int hashCode() {
    return Objects.hash(primaryKey);
  }
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), primaryKey);
  }
}