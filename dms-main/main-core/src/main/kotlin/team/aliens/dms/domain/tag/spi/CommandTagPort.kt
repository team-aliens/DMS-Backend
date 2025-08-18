package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface CommandTagPort {

    fun saveTag(tag: Tag): Tag

    fun deleteTagById(tagId: UUID)
}
