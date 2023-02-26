package team.aliens.dms.persistence.point.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.point.entity.PhraseJpaEntity

@Component
class PhraseMapper : GenericMapper<Phrase, PhraseJpaEntity> {

    override fun toDomain(entity: PhraseJpaEntity?): Phrase? {
        return entity?.let {
            Phrase(
                id = entity.id,
                content = entity.content,
                type = entity.type,
                standard = entity.standard
            )
        }
    }

    override fun toEntity(domain: Phrase): PhraseJpaEntity {
        return PhraseJpaEntity(
            id = domain.id,
            content = domain.content,
            type = domain.type,
            standard = domain.standard
        )
    }
}
