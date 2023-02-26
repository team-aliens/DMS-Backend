package team.aliens.dms.thirdparty.storage

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Mimetypes
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.stereotype.Component
import team.aliens.dms.domain.file.exception.FileIOInterruptedException
import team.aliens.dms.domain.file.spi.UploadFilePort
import java.io.File
import java.io.IOException

@Component
class AwsS3Adapter(
    private val awsProperties: AwsS3Properties,
    private val amazonS3Client: AmazonS3Client
) : UploadFilePort {

    override fun upload(file: File): String {
        inputS3(file, file.name)

        return getResource(file.name)
    }

    private fun inputS3(file: File, fileName: String) {
        try {
            val inputStream = file.inputStream()
            val objectMetadata = ObjectMetadata().apply {
                this.contentLength = file.length()
                this.contentType = Mimetypes.getInstance().getMimetype(file)
            }

            amazonS3Client.putObject(
                PutObjectRequest(awsProperties.bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(
                        CannedAccessControlList.PublicRead
                    )
            )

            file.delete()
        } catch (e: IOException) {
            throw FileIOInterruptedException
        }
    }

    private fun getResource(fileName: String): String {
        return amazonS3Client.getResourceUrl(awsProperties.bucket, fileName)
    }
}
