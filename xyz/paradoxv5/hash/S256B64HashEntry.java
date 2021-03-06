package xyz.paradoxv5.hash;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Scanner;

/**
  {@link HashEntry} that use {@link MessageDigest SHA-256} &
  {@link Base64}-{@linkplain Encoder#withoutPadding() no-padding (‘=’)}
  to hash-transform from Strings to Strings
  
  @param <K>
    The type of the HashEntry key
  @version 1.1
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
  
  /**
    A persistent constant reference to the Basic
    {@linkplain Encoder#withoutPadding() no-padding (‘=’)}
    Base64 encoder
  */
  static Encoder BASE64ENCODER = Base64.getEncoder().withoutPadding();
  /** A persistent constant reference to the Basic Base64 decoder */
  static Base64.Decoder BASE64DECODER = Base64.getDecoder();
  
  /**
    [API] Generate the hash data before the {@link Base64} part,
    used by {@link #hash(String, byte[])} which is
    used by {@link #accept(Object, byte[])}
    
    @param string
      the string
    @param salt
      the {@link #getSalt() salt}
    @return
      the resulting hash data before {@linkplain #BASE64ENCODER Base64 encoding}
    @see S256B64HashEntry
      Interface Javadoc
    
    @apiNote
      {@linkplain MessageDigest#reset() Reset} the {@link #DIGEST},
      {@linkplain MessageDigest#update(byte[]) feed} in first the {@code salt}
      then the {@link #UTF_8} {@linkplain String#getBytes() bytes} of
      {@code string}, and finally {@code return} the
      {@linkplain MessageDigest#digest(byte[]) result}.
  */
  static byte[] digest(String string, byte[] salt) {
    DIGEST.reset();
    DIGEST.update(salt);
    return DIGEST.digest(
      string.getBytes(UTF_8)
    );
  }
  /**
    [API] Generate the hash data of the given parameters,
    used by {@link #accept(Object, byte[])}
    
    @param
      string the string
    @param
      salt the salt
    @return
      the hash data
    @see S256B64HashEntry
      Interface Javadoc
    @apiNote
      Returns the {@linkplain #BASE64ENCODER Base64-encoded},
      {@linkplain Encoder#withoutPadding() no padding}
      String of {@link #digest(String, byte[])}.
  */
  static String hash(String string, byte[] salt) {
    return BASE64ENCODER.encodeToString(digest(string, salt));
  }
  
  /**
    @apiNote
      As stated in the {@linkplain S256B64HashEntry Interface Javadoc}, the algorithm
      is {@link MessageDigest SHA-256} (provided by {@link #digest(String, byte[])}) +
      {@link #BASE64ENCODER Base64}-{@linkplain Encoder#withoutPadding() no-padding}
      (provided by {@link #hash(String, byte[])}).
    @implNote
      Note that, as an {@code interface}, this does <strong>not</strong> ship
      any implementation to track the {@code salt} for {@link #getSalt()} and
      thus still need to be implemented by subclasses.
  */
  @Override default void accept(String string, byte[] salt) {
    setValue(hash(string, salt));
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
  
  /**
    Print the {@link #hash(String, byte[]) hash data} of using the
    {@linkplain Scanner#nextLine() next two lines} from {@link System#in}
    as the {@code string} and {@code salt} respectively.
    @param __ Command-line arguments (unused)
  */
  static void main(String[] __) {
    try(Scanner scanner = new Scanner(System.in)) {
      System.out.println(hash(
        scanner.nextLine(),
        scanner.nextLine().getBytes(UTF_8)
      ));
    }
  }
}