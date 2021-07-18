package xyz.paradoxv5.servlet;

import java.util.Set;
import java.io.Serializable;
// JavaDoc imports
import java.util.Collection;

/**
  This is a contract interface identifying that a type supports the CRUD operations:<ul>
    <li>Create: {@link #add(Serializable)}
    <li>Read: {@link #get(Object)} (and {@link #getAll()} and {@link #getAndRemove(Object)})
    <li>Update: {@link #update(Serializable)}
    <li>Delete: {@link #remove(Serializable)} (and {@link #getAndRemove(Object)})
  </ul>
  This interface is akin to non-{@link Iterable} {@link Collection}s.
  Implementations themselves determine if they’d like to synchronize these access operations.
  
  @param <E>
    The type of elements the implementing type CRUDs
  @param <K>
    The type of the elements’ ID (e.g. Primary Key), primarily for Read operations
*/
public interface CRUD<E extends Serializable, K> {
  /**
    @param primaryKey
      the ID
    @return
      the element of that key
    @see #getAll()
    @see #getAndRemove(Object)
  */
  public E get(K primaryKey);
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
    @param object
      the element to add
    @throws Exception
      If a checked exception might throw
  */
  public void add(E object) throws Exception;
  /**
    @param object
      the element to update to
    @throws Exception
      If a checked exception might throw
    @implSpec
      Implementations shall use the ID of the {@code object} parameter to determine the element to update to it.
      Therefore, implementation optionally may {@code throw} if the given ID doesn’t currently exist,
      or simply redirect to {@link #add(Serializable)}.
  */
  public void update(E object) throws Exception;
  /**
    @param object
      the element to remove
    @throws Exception
      If a checked exception might throw
    @implSpec
      Implementations shall use the ID of the {@code object} parameter to determine the element to remove.
      Implementations optionally may require additional synchronized fields in addition to the ID to match
      in order to ensure synchronization, and optionally may {@code throw} if no matches currently exist.
    @see
      #getAndRemove(Object)
  */
  public void remove(E object) throws Exception;
  
  /**
    @param primaryKey
      the ID of the element to get and remove
    @return
      the element of that ID, now removed
    @throws Exception
      If a checked exception might throw
    @implSpec
      See {@link #remove(Serializable)} for Impl.Specs. regarding removing elements
    @see
      #get(Object)
      #remove(Serializable)
  */
  public default E getAndRemove(K primaryKey) throws Exception {
    E object = get(primaryKey);
    remove(object);
    return object;
  }
}