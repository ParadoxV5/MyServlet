package xyz.paradoxv5.hash;
import java.security.MessageDigest;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
  {@link HashEntry} that use {@link MessageDigest SHA-256} &
  {@link Base64} to hash-transform from Strings to Strings
  
  @param <K>
    The type of the HashEntry key
  @version 1
*/
public interface S256B64HashEntry<K> extends HashEntry<K, String, String> {
  /**
    {@linkplain MessageDigest#getInstance(String) Find} the SHA-256 digest
    @return the digest if found, {@code null} if not
    @apiNote This is for initializing the persistent constant {@link #DIGEST}.
  */
  static MessageDigest getDigest() {
    MessageDigest digest = null; try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch(java.security.NoSuchAlgorithmException __) {
      // Shouldn’t happen ?
      System.err.println(
        "This JDK is incomplete in that it does not support a SHA-256 digest."
      );
    }
    return digest;
  }
  /**
    A persistent constant reference to the SHA-256
    digest provided by {@link #getDigest()}
  */
  static MessageDigest DIGEST = getDigest();
  
  /** A persistent constant reference to the Basic Base64 encoder */
  static Base64.Encoder BASE64ENCODER = Base64.getEncoder();
  /** A persistent constant reference to the Basic Base64 decoder */
  static Base64.Decoder BASE64DECODER = Base64.getDecoder();
  
  /**
    {@linkplain MessageDigest#reset() Reset} the {@link #DIGEST},
    {@linkplain MessageDigest#update(byte[]) feed} in first the {@code salt}
    then the {@link #UTF_8} {@linkplain String#getBytes() bytes} of
    {@code string}, and finally {@code return} the
    {@linkplain MessageDigest#digest(byte[]) result}.
    
    @param string the string
    @param salt the salt
    @return the resulting hash data
    @see #accept(Object, byte[])
  */
  static byte[] digest(String string, byte[] salt) {
    DIGEST.reset();
    DIGEST.update(salt);
    return DIGEST.digest(
      string.getBytes(UTF_8)
    );
  }
  /** @apiNote
    As stated in the {@linkplain S256B64HashEntry Interface Javadoc},
    The algorithm is {@link MessageDigest SHA-256} (provided by
    {@link #digest(String, byte[])}) + {@link #BASE64ENCODER Base64}.
  */
  @Override default void accept(String string, byte[] salt) {
    setValue(BASE64ENCODER.encodeToString(digest(string, salt)));
  }
  
  /** Check if this entry matches the given {@code string}.
    @param string the string to compare with
    @return {@code true/false} whether they match
    @apiNote
      This checks by first {@link #digest(String, byte[])} the {@code string}
      with {@linkplain #getSalt() the same salt used}, then
      {@linkplain MessageDigest#isEqual(byte[], byte[]) compare}
      the resulting hash data with the {@linkplain #BASE64DECODER decoded}
      {@linkplain #getValue() hash data this instance recorded}.
  */
  default boolean matches(String string) {
    return MessageDigest.isEqual(
      BASE64DECODER.decode(getValue()),
      digest(string, getSalt())
    );
  }
}