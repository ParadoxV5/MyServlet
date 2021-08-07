package xyz.paradoxv5;
import java.util.UUID;
import java.math.BigInteger;

import java.nio.ByteBuffer;
/** {@link UUID} utilities 
  @version 1.1
*/
public class UUIDUtil {
  /** No instantiating */
  protected UUIDUtil() {}
  
  /** Convert an UUID to a BigInteger
    @param uuid
      the UUID
    @return
      The BigInteger of the UUID’s value (according to its bits); nonnegative
    @apiNote
      Uses a {@link ByteBuffer} to hold {@link UUID#getMostSignificantBits()}
      and {@link UUID#getLeastSignificantBits()}
  */
  public static BigInteger toBigInteger(UUID uuid) {
    // Pad a byte of 0s so the result is always positive
    ByteBuffer buffer = ByteBuffer.wrap(new byte[17], 1, 16);
    buffer.putLong(uuid.getMostSignificantBits());
    buffer.putLong(uuid.getLeastSignificantBits());
    return new BigInteger(buffer.array());
  }
  
  /** Convert a BigInteger to an UUID
    @param bigInteger
      the BigInteger
    @return
      the UUID whose value is (the
      {@linkplain BigInteger#longValue() 128 lowest bits of} the
      {@linkplain BigInteger#abs() absolute value of}) the BigInteger
  */
  public static UUID fromBigInteger(BigInteger bigInteger) {
    bigInteger = bigInteger.abs();
    return new UUID(
      bigInteger.shiftRight(64).longValue(),
      bigInteger.longValue()
    );
  }
  
  /** A BigInteger representing the bits an UUID uses: 2<sup>128</sup>-1
    @see #preprocessBigInteger(BigInteger)
  */
  protected static final BigInteger BIGINTEGER_MASK =
    BigInteger.ZERO.setBit(128).subtract(BigInteger.ONE);
  /**
    Process the given BigInteger so the result is guaranteed
    to reflect {@linkplain #toBigInteger(UUID) an UUID value}
    @param bigInteger
      the input BigInteger
    @return
      the processed BigInteger; specifically the
      {@linkplain #BIGINTEGER_MASK lower 128 bits} of the
      input-{@linkplain BigInteger#abs() without-the-signum}
  */
  public static BigInteger preprocessBigInteger(BigInteger bigInteger) {
    return bigInteger.abs().and(BIGINTEGER_MASK);
  }
}