package team.aliens.dms.persistence.point.entity

import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_point_option")
class PointOptionJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT", nullable = false)
    val score: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val type: PointType

) : BaseUUIDEntity(id)
