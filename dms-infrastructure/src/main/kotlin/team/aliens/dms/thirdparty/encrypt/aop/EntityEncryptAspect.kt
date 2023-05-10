package team.aliens.dms.thirdparty.encrypt.aop

import java.lang.reflect.Field
import java.util.function.Consumer
import java.util.function.Function
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import team.aliens.dms.common.annotation.Decrypted
import team.aliens.dms.common.annotation.EncryptType
import team.aliens.dms.common.annotation.EncryptedColumn
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.EncryptPort
import team.aliens.dms.common.spi.SchoolSecretPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException

@UseCase
@Aspect
class EntityEncryptAspect(
    private val securityPort: SecurityPort,
    private val schoolSecretPort: SchoolSecretPort,
    private val encryptPort: EncryptPort
) {

    @AfterReturning(
        "@annotation(team.aliens.dms.common.annotation.Encrypt)",
        returning = "ret"
    )
    fun encryptReturnValue(joinPoint: JoinPoint, ret: Any) {
        if (ret is List<*>) ret.map { obj ->
            doOnEncryptedColumn(obj!!) { field ->
                setEncryptedValue(obj, field)
            }
        } else {
            doOnEncryptedColumn(ret) { field ->
                setEncryptedValue(ret, field)
            }
        }
    }

    @AfterReturning(
        "@annotation(team.aliens.dms.common.annotation.Decrypt)",
        returning = "ret"
    )
    fun decryptReturnValue(joinPoint: JoinPoint, ret: Any) {
        if (ret is List<*>) ret.map { obj ->
            doOnEncryptedColumn(obj!!) { field ->
                setDecryptedValue(obj, field)
            }
        } else {
            doOnEncryptedColumn(ret) { field ->
                setDecryptedValue(ret, field)
            }
        }
    }

    @Around("execution(* *(.., @team.aliens.dms.common.annotation.Decrypted() *, ..))+")
    fun decryptParameterValue(joinPoint: ProceedingJoinPoint) {
        (joinPoint.signature as MethodSignature).method.parameters
            .forEachIndexed { idx, param ->
                if (param.annotations.contains(Decrypted::class as Annotation)) {
                    doOnEncryptedColumn(joinPoint.args[idx]) { field ->
                        setDecryptedValue(joinPoint.args[idx], field)
                    }
                }
            }
    }

    private fun doOnEncryptedColumn(obj: Any, behavior: Consumer<Field>) {
        obj::class.java.declaredFields.map { field: Field ->
            if (field.isAnnotationPresent(EncryptedColumn::class.java)) {
                behavior.accept(field)
            }
        }
    }

    private fun setEncryptedValue(obj: Any, field: Field) {
        getAnnotationAndSet(obj, field) { f ->
            when (f.getAnnotation(EncryptedColumn::class.java).type) {
                EncryptType.SYMMETRIC -> encryptPort.symmetricEncrypt(
                    secretKey = getSchoolKey(),
                    plainText = f[obj] as String
                )
                EncryptType.ASYMMETRIC -> encryptPort.asymmetricEncrypt(
                    plainText = f[obj] as String
                )
            }
        }
    }

    private fun setDecryptedValue(obj: Any, field: Field) {
        getAnnotationAndSet(obj, field) { f ->
            when (f.getAnnotation(EncryptedColumn::class.java).type) {
                EncryptType.SYMMETRIC -> encryptPort.symmetricDecrypt(
                    secretKey = getSchoolKey(),
                    cipherText = f[obj] as String
                )
                EncryptType.ASYMMETRIC -> encryptPort.asymmetricDecrypt(
                    cipherText = f[obj] as String
                )
            }
        }
    }

    private fun getAnnotationAndSet(obj: Any, field: Field, function: Function<Field, String>) {
        field.run {
            check(type == String::class.java)
            isAccessible = true
            set(obj, function.apply(this))
        }
    }

    private fun getSchoolKey(): String {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val schoolSecret = schoolSecretPort.querySchoolSecretBySchoolId(schoolId) ?: throw SchoolNotFoundException
        return encryptPort.asymmetricDecrypt(schoolSecret)
    }
}
