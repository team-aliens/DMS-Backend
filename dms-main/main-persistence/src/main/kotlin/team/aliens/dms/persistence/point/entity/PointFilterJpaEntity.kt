package team.aliens.dms.persistence.point.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID

@Entity
@Table(
    name = "tbl_point_filter",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "name"))
    ]
)
class PointFilterJpaEntity(

    id: UUID?,

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
