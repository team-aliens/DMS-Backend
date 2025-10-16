package team.aliens.dms.global.security.passport

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.PassportUser
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class PassportIntegrityGenerator(
    private val passportProperties: PassportProperties
) {
    fun generate(passportUser: PassportUser): String {
        val data = "${passportUser.id}|${passportUser.schoolId}|${passportUser.authority}"
        val hmacKey = SecretKeySpec(passportProperties.secretKey.encoded, PassportSecurityProperties.HMAC_ALGORITHM)
        val mac = Mac.getInstance(PassportSecurityProperties.HMAC_ALGORITHM)
        mac.init(hmacKey)
        val hmacBytes = mac.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(hmacBytes)
    }
}
