package xyz.paradoxv5.jpa;
import javax.persistence.EntityManager;
import java.util.function.Supplier;
/**
  A very simple concrete class for {@link AbstractEntityDB} which
  implements the abstract methods with the bare-neccessity JPA calls.
  @param <E> The type of entity elements of an instance DB
  @param <K> he type of the primary key of the entity elements of an instance DB.
  @version 1
*/
public class SimpleEntityDB<E extends AbstractEntity<K>, K extends java.io.Serializable> extends AbstractEntityDB<E, K> {

  /**
    {@link SimpleEntityDB} constructor, which simply proxies all parameters to the {@link AbstractEntityDB}
    {@linkplain AbstractEntityDB#AbstractEntityDB(Class, String, Supplier) constructor}
    @param e {@link #e}
    @param ql_getAll {@link #ql_getAll}
    @param emWrapperSupplier {@link #emWrapperSupplier}
  */
  public SimpleEntityDB(Class<E> e, String ql_getAll, Supplier<EntityManagerWrapper> emWrapperSupplier) {
    super(e, ql_getAll, emWrapperSupplier);
  }
  
  /** @apiNote This simple implementation uses {@link EntityManager#persist(Object)}. */
  @Override protected void add0(EntityManager entityManager, E entity) throws Exception {
    entityManager.persist(entity);
  }
  
  /** @apiNote This simple implementation uses {@link EntityManager#merge(Object)}. */
  @Override protected void update0(EntityManager entityManager, E entity) throws Exception {
    entityManager.merge(entity);
  }
  
  /** @apiNote This simple implementation uses {@link EntityManager#remove(Object)}. */
  @Override protected void remove0(EntityManager entityManager, E entity) throws Exception {
    entityManager.remove(entity);
  }
}