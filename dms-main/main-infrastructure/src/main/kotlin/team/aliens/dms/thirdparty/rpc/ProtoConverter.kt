package team.aliens.dms.thirdparty.rpc

import com.google.protobuf.ByteString
import java.nio.ByteBuffer
import java.util.UUID

fun UUID.toByteString(): ByteString {
    val buffer = ByteBuffer.allocate(16)
    buffer.putLong(mostSignificantBits)
    buffer.putLong(leastSignificantBits)
    return ByteString.copyFrom(buffer.array())
}

fun ByteString.toUUID(): UUID {
    val buffer = asReadOnlyByteBuffer()
    return UUID(buffer.long, buffer.long)
}
