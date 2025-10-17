package team.aliens.dms.thirdparty.notification

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream

@Configuration
class FCMConfig(
    @Value("\${fcm.credentials-base64}")
    private val credentialsBase64: String
) {
    @PostConstruct
    fun initialize() {
        try {

            val credentialsBytes = java.util.Base64.getDecoder().decode(credentialsBase64)
            val inputStream = ByteArrayInputStream(credentialsBytes)

            if (FirebaseApp.getApps().isEmpty()) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build()
                FirebaseApp.initializeApp(options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
