package team.aliens.dms.persistence.point.entity

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import team.aliens.dms.common.annotation.EncryptType
import team.aliens.dms.common.annotation.EncryptedColumn
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Entity
@Table(name = "tbl_point_history")
class PointHistoryJpaEntity(

    id: UUID?,

    @EncryptedColumn(type = EncryptType.SYMMETRIC)
    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val studentName: String,

    @EncryptedColumn(type = EncryptType.SYMMETRIC)
    @Column(columnDefinition = "TEXT", nullable = false)
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
