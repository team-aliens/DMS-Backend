package team.aliens.dms.thirdparty.encrypt

import com.amazonaws.services.kms.AWSKMSAsync
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptRequest
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EncryptPort
import team.aliens.dms.thirdparty.encrypt.exception.KMSException
import java.nio.ByteBuffer
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class AwsKMSAdapter(
    private val awskmsAsync: AWSKMSAsync,
    private val awsKMSProperties: AwsKMSProperties,
    private val encryptProperties: EncryptProperties
) : EncryptPort {

    override fun symmetricEncrypt(
        secretKey: String,
        plainText: String,
    ): String {
        val cipher = getCipher(Cipher.ENCRYPT_MODE, secretKey)
        return String(
            cipher.doFinal(plainText.toByteArray()).encodeBase64()
        )
    }

    override fun symmetricDecrypt(
        secretKey: String,
        cipherText: String,
    ): String {
        val cipher = getCipher(Cipher.DECRYPT_MODE, secretKey)
        return String(
            cipher.doFinal(cipherText.decodeBase64())
        )
    }

    @Cacheable("cipher")
    protected fun getCipher(opmode: Int, key: String): Cipher =
        Cipher.getInstance(encryptProperties.transformation).apply {
            init(opmode, SecretKeySpec(key.toByteArray(), encryptProperties.algorithm))
        }

    override fun asymmetricEncrypt(plainText: String): String {

        val request = EncryptRequest()
            .withKeyId(awsKMSProperties.key)
            .withPlaintext(ByteBuffer.wrap(plainText.toByteArray()))
            .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)

        val encryptedByte = runCatching {
            awskmsAsync.encrypt(request).ciphertextBlob.array()
        }.onFailure { e ->
            e.printStackTrace()
            throw KMSException
        }.getOrThrow()

        return String(
            encryptedByte.encodeBase64()
        )
    }

    @Cacheable("decryptedText")
    override fun asymmetricDecrypt(cipherText: String): String {

        val request = DecryptRequest()
            .withKeyId(awsKMSProperties.key)
            .withCiphertextBlob(ByteBuffer.wrap(cipherText.decodeBase64()))
            .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)

        val decryptedByte = runCatching {
            awskmsAsync.decrypt(request).plaintext.array()
        }.onFailure { e ->
            e.printStackTrace()
            throw KMSException
        }.getOrThrow()

        return String(decryptedByte)
    }

    // byteArray를 String으로 바로 변환하면 padding이 깨지는 문제가 발생하기 때문에
    // encoding 결과를 Base64로 암호화하여 저장한다.
    private fun ByteArray.encodeBase64() = Base64.getEncoder().encode(this)
    private fun String.decodeBase64() = Base64.getDecoder().decode(this)
}
