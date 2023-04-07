package team.aliens.dms.common.dto

class PageData(
    page: Long?,
    size: Long?
) {

    val page: Long = page ?: PAGE_NUM
    val size: Long = size ?: PAGE_SIZE

    val offset: Long
        get() = page * size

    companion object {
        private const val PAGE_NUM: Long = 0
        private const val PAGE_SIZE: Long = 500

        val DEFAULT = PageData(null, null)
    }
}
