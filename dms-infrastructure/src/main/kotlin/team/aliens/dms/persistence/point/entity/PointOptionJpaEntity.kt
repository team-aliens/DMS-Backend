package team.aliens.dms.persistence.point.entity

import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "tbl_point_option")
class PointOptionJpaEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT", nullable = false)
    val score: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val type: PointType

) : BaseUUIDEntity()