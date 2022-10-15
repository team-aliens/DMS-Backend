package team.aliens.dms.global.error

abstract class DmsException(
    val errorProperty: ErrorProperty
): RuntimeException() {

    override fun fillInStackTrace() = this
}