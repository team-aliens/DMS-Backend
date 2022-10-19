package team.aliens.dms.global.spi

interface GenerateRandomNumberStringPort {

    fun getRandomNumberString(number: Int): String
}