package team.aliens.dms.common.annotation

import java.lang.annotation.Inherited

/**
 * 저장시 암호화되는 컬럼을 명시해주기 위한 어노테이션
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class EncryptedColumn(
    val type: EncryptType
)

enum class EncryptType {
    SYMMETRIC,
    ASYMMETRIC
}

/**
 * 메서드의 반환값에서 EncryptedColumn이 붙은 필드을 암호화하는 어노테이션
 * @see EncryptedColumn
 * @see team.aliens.dms.persistence.EncryptableGenericMapper
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Encrypt

/**
 * 메서드의 반환값에서 EncryptedColumn이 붙은 필드을 복호화하는 어노테이션
 * @see EncryptedColumn
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Decrypt

/**
 * 파라미터 전달시에 암호화된 엔티티를 복호화하는 어노테이션
 * @see team.aliens.dms.persistence.EncryptableGenericMapper
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Decrypted(
    val value: String = ""
)
