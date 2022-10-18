package team.aliens.dms.global.spi

interface CoveredEmailPort {
    fun coveredEmail(email: String) : String
}