package team.aliens.dms.persistence.point.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_point_history")
class PointHistoryJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val studentName: String,

    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val studentGcn: String,

    @Column(columnDefinition = "INT", nullable = false)
    val bonusTotal: Int,

    @Column(columnDefinition = "INT", nullable = false)
    val minusTotal: Int,

    @Column(columnDefinition = "BIT(1)", nullable = false)
    val isCancel: Boolean,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val pointName: String,

    @Column(columnDefinition = "INT", nullable = false)
    val pointScore: Int,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val pointType: PointType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    override val createdAt: LocalDateTime

) : BaseEntity(id)
