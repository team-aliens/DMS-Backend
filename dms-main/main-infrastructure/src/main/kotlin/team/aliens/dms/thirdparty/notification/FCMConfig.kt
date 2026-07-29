package team.aliens.dms.thirdparty.notification

import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.util.Base64

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
