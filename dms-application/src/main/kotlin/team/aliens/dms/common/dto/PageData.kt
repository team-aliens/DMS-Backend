package team.aliens.dms.common.dto

class PageData(
    page: Long?,
    size: Long?
) {

    val page: Long = page ?: 0
    val size: Long = size ?: 500

    val offset: Long
        get() = page * size

    companion object {
        val DEFAULT = PageData(null, null)
    }
}