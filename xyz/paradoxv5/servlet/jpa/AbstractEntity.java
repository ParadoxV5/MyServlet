package xyz.paradoxv5.servlet.jpa;

import java.util.Objects;
import java.io.Serializable;
// Javadoc imports not used by Java code
import xyz.paradoxv5.servlet.CRUD;

/**
  My abstract superclass for an entity of {@linkplain AbstractEntityDB a database}.
  <p>
  This superclass effectively only has default methods of technical use:<ul>
    <li>{@link #equals(Object)}
    <li>{@link #hashCode()}
    <li>{@link #toString()}
  </ul>
  
  @param <K>
    The type of {@link #primaryKey() the primary key}
  @version
    1
*/
public abstract class AbstractEntity<K extends Serializable> implements Serializable {
  private static final long serialVersionUID = 0;
  
  /** @return the Primary Key (i.e. the ID of the type’s {@link CRUD} manager) */
  protected abstract K primaryKey();
  
  /** @return
    {@code true} if {@code other} is the same type as {@code this} and they have
    equal (via {@link Objects#equals(Object, Object)}) {@link #primaryKey()};
    {@code false} otherwise
  */
  @Override public boolean equals(Object other) {
    return this == other || (
      getClass().isInstance(other) &&
      Objects.equals(primaryKey(), ((AbstractEntity<?>)other).primaryKey())
    );
  }
  /** @return
    the {@link Objects#hash(Object...)} of {@linkplain Class#getName() the instance’s class’s name}
    and {@link #primaryKey()}
  */
  @Override public int hashCode() {
    return Objects.hash(getClass().getName(), primaryKey());
  }
  /** @return
    a String of the format:
    {@link Class#getSimpleName() ClassSimpleName}{@code <}{@link #primaryKey() primaryKey}{@code >}
  */
  @Override public String toString() {
    return String.format("%s<%s>", getClass().getSimpleName(), primaryKey());
  }
}