package team.aliens.dms.persistence.point.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID

@Entity
@Table(name = "tbl_phrase")
class PhraseJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val type: PointType,

    @Column(columnDefinition = "INT", nullable = false)
    val standard: Int

) : BaseUUIDEntity(id)
