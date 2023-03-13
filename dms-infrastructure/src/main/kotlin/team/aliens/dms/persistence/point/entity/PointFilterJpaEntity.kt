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
@Table(name = "tbl_point_filter")
class PointFilterJpaEntity(

    override val id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val pointType: PointType,

    @Column(columnDefinition = "INT", nullable = false)
    val maxPoint: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val minPoint: Int

) : BaseUUIDEntity(id)