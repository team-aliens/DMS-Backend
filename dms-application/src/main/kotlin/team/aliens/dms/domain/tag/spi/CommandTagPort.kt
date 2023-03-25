package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.Tag

interface CommandTagPort {

    fun saveTag(tag: Tag): Tag
}
