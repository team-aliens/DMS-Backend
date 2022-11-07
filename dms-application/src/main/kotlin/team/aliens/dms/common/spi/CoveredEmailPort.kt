package team.aliens.dms.common.spi

interface CoveredEmailPort {

    fun coveredEmail(email: String) : String

}