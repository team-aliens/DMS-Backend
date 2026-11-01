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
                    .build()

                FirebaseApp.initializeApp(options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
