package team.aliens.dms.common.spi

interface GCNToStringPort {
    fun gcnToString(grade: Int, classRoom: Int, number: Int): String
}