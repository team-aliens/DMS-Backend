package team.aliens.dms.common.dto

data class PageData(
    val page: Long = 0,
    val size: Long = 10000
) {
    val offset: Long
        get() = page * size

    companion object {
        val DEFAULT = PageData()
    }
}