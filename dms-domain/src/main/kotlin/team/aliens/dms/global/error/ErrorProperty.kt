package team.aliens.dms.global.error

interface ErrorProperty {
    fun status(): Int
    fun message(): String
}