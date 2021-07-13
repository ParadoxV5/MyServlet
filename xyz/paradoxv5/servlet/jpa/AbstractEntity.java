package xyz.paradoxv5.servlet.jpa;
import java.util.Objects;
import java.io.Serializable;

/**
  
*/
public abstract class AbstractEntity<K extends Serializable> implements Serializable {
  private static final long serialVersionUID = 0;
  
  protected abstract K primaryKey();
  
  @Override public boolean equals(Object other) {
    return this == other || (
      getClass().isInstance(other) &&
      Objects.equals(primaryKey(), ((AbstractEntity<?>)other).primaryKey())
    );
  }
  @Override public int hashCode() {
    return Objects.hash(primaryKey());
  }
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), primaryKey());
  }
}