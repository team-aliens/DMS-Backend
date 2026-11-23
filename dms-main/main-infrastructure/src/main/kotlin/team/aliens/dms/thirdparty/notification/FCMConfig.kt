package team.aliens.dms.thirdparty.notification

import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.ByteArrayInputStream
import java.util.Base64

// dev 는 실제 학생 기기로 알림이 나가지 않도록 Firebase 를 초기화하지 않는다 — NoOpNotificationAdapter 참고
@Profile("!dev")
@Configuration
class FCMConfig(
    @Value("\${fcm.credentials-base64}")
    private val credentialsBase64: String
) {

    @PostConstruct
    fun initialize() {

        val credentialsBytes = Base64.getDecoder().decode(credentialsBase64)
        val inputStream = ByteArrayInputStream(credentialsBytes)

        try {
            if (FirebaseApp.getApps().isEmpty()) {
                val scopedCredentials = GoogleCredentials.fromStream(inputStream)

                val options = FirebaseOptions.builder()
                    .setCredentials(scopedCredentials)
                    .setHttpTransport(ApacheHttpTransport())
                    .setConnectTimeout(CONNECT_TIMEOUT_MILLIS)
                    .setReadTimeout(READ_TIMEOUT_MILLIS)
                    .build()

                FirebaseApp.initializeApp(options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        // SDK 기본값 0은 무제한이다. outbox 발송은 행 잠금과 DB 커넥션을 쥔 채 FCM 을 기다리므로 상한을 둔다
        // 커넥션 풀이 호스트당 20개라 토큰이 많으면 (토큰 수 / 20) 배까지 기다릴 수 있다
        private const val CONNECT_TIMEOUT_MILLIS = 3_000
        private const val READ_TIMEOUT_MILLIS = 5_000
    }
}
