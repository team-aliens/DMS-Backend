package team.aliens.dms.common.util

object FileUtil {

    fun isCorrectExtension(extension: String) =
        when (extension.lowercase()) {
            "jpg", "jpeg", "png", "heic" -> true
            else -> false
        }
}
