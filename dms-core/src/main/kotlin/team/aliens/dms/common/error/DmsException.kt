package team.aliens.dms.common.error

abstract class DmsException(
    val errorProperty: ErrorProperty
) : RuntimeException()