package xyz.paradoxv5.data;

import java.util.Set;
import java.io.Serializable;
// Javadoc imports not used by Java code
import java.util.Collection;

/**
  This is a contract interface identifying that a type supports the CRUD operations:<ul>
    <li>Create: {@link #add(Serializable)}
    <li>Read:   {@link #get(Object)} and {@link #getAll()}
    <li>Update: {@link #update(Serializable)}
    <li>Delete: {@link #remove(Serializable)}
  </ul>
  This interface is akin to non-{@link Iterable} {@link Collection}s.
  Implementations themselves determine if they’d like to synchronize these access operations.
  
  @param <E>
    The type of elements the implementing type CRUDs
  @param <K>
    The type of the elements’ ID (e.g. Primary Key), primarily for Read operations
  @version
    1.1
*/
public interface CRUD<E extends Serializable, K> {
  /**
    @param id
      the ID
    @throws Exception
      If a checked exception might throw
    @return
      the element of that key
    @see #getAll()
  */
  public E get(K id) throws Exception;
  /**
    @return
      A set of all elements managed by this CRUD type
    @throws Exception
      If a checked exception might throw
    @apiNote
      It is expected that CRUD types manage an non-indexed collection of distinct elements,
      thus the choice of a {@link Set} as this method’s return type.
    @see
      #get(Object)
  */
  public Set<E> getAll() throws Exception;
  
  /**
    @param element
      the element to add
    @throws Exception
      If a checked exception might throw
  */
  public void add(E element) throws Exception;
  /**
    @param element
      the element to update to
    @throws Exception
      If a checked exception might throw
    @implSpec
      Implementations shall use the ID of the {@code element} parameter to determine the element to update to it.
      Therefore, implementation optionally may {@code throw} if the given ID doesn’t currently exist,
      or simply redirect to {@link #add(Serializable)}.
  */
  public void update(E element) throws Exception;
  /**
    @param element
      the element to remove
    @throws Exception
      If a checked exception might throw
    @implSpec
      Implementations shall use the ID of the {@code element} parameter to determine the element to remove.
      Implementations optionally may require additional synchronized fields in addition to the ID to match
      in order to ensure synchronization, and optionally may {@code throw} if no matches currently exist.
  */
  public void remove(E element) throws Exception;
}