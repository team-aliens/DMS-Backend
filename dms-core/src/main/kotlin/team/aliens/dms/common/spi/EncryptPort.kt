package team.aliens.dms.common.spi

interface EncryptPort {

    fun symmetricEncrypt(secretKey: String, plainText: String): String

    fun symmetricDecrypt(secretKey: String, cipherText: String): String

    fun asymmetricEncrypt(plainText: String): String

    fun asymmetricDecrypt(cipherText: String): String
}
