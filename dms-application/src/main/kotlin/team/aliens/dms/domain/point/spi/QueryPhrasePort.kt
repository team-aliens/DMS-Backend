package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType

interface QueryPhrasePort {

    fun queryPhraseAll(): List<Phrase>

    fun queryPhraseAllByPointTypeAndStandardPoint(type: PointType, point: Int): List<Phrase>

}