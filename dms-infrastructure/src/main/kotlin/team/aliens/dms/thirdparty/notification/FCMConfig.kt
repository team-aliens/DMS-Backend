package team.aliens.dms.thirdparty.notification

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.File
import java.io.IOException
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FCMConfig(
    @Value("\${fcm.path}")
    private val path: String
) {

    @PostConstruct
    fun initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(File(path).inputStream()))
                    .build()
                FirebaseApp.initializeApp(options)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
