package team.aliens.dms.persistence.point.entity

import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tbl_phrase")
class PhraseJpaEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val content: String,

    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val type: PointType,

    @Column(columnDefinition = "INT", nullable = false)
    val standard: Int

) : BaseUUIDEntity()