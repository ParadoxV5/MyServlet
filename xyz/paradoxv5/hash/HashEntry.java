package xyz.paradoxv5.hash;
import java.util.Map;
import java.util.function.*;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
  A key-value pair of a {@link #accept(Object, byte[]) hashing operation}’s
  records, where the {@link #getKey() key} is for identification and the
  {@code byte[]} {@link #getValue() value} the resulting hash
  
  @param <K> The type of the key
  @param <T> The type of the object that was / will be hashed
  @see #accept(Object, byte[])
*/
public interface HashEntry<K, T> extends
  Map.Entry<K, byte[]>, Supplier<byte[]>,
  BiConsumer<T, byte[]>, Consumer<T>,
  java.io.Serializable
{
  /**
    Hash, with some algorithm from the implementation, the supplied
    {@code object} with the preconfigured variable {@link #getSalt() salt}
    and optionally the constant {@link #getKey()}, and
    {@linkplain #setValue(byte[]) store the resulting hashed data}.
    
    @param object
      the object to hash and store
    @param salt
      the salt data; later retrievable by {@link #getSalt()}
    @see #accept(Object)
    @implNote
      This is where to code or call the hashing algorithm,
      such as one provided by {@link java.security.MessageDigest}.
    @implSpec
      The implementation should sastisfy the
      expectations set by {@link Object#hashCode()}.
      <p>
      Don’t forget to store the {@code salt} so it’s
      later accessible through {@link #getSalt()}.
  */
  @Override void accept(T object, byte[] salt);
  /**
    {@link #accept(Object, byte[])} with {@linkplain #getSalt()}
    as the {@code salt} parameter.
  */
  @Override default void accept(T object) { accept(object, getSalt()); }
  
  /** Alias of {@link #getValue()} */
  @Override default byte[] get() { return getValue(); }
  /** @return The hashed data
    @see #setValue(byte[])
  */
  @Override byte[] getValue();
  /** Set the hash data directly
    @param hash the hash data
    @return
      According to the contract set by
      {@link java.util.Map.Entry#setValue(Object)},
      the previous hash before overwriting
    @see #accept(Object, byte[])
    @see #getValue()
  */
  @Override byte[] setValue(byte[] hash);
  
  /** @return the salt data associated with the last hashing operation.
    @apiNote
      The default implementation returns the {@link #UTF_8}
      {@linkplain String#getBytes(java.nio.charset.Charset) bytes} of the
      {@link Object#toString()} of {@link #getKey()}.
    @implNote
      This one does not neccessarily have to be secure.
    @implSpec
      Before any {@link #accept(Object, byte[])} calls, implementations
      may return a default salt value or an empty {@code byte[]} or, if
      the implementation permits {@code null} salt values, {@code null}.
  */
  default byte[] getSalt() {
    return getKey().toString().getBytes(UTF_8);
  }
}