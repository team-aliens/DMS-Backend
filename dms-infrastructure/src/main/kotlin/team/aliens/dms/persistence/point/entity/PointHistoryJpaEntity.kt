package team.aliens.dms.persistence.point.entity

import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_point_history")
class PointHistoryJpaEntity(

    override val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_option_id", columnDefinition = "BINARY(16)")
    val pointOptionJpaEntity: PointOptionJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val studentJpaEntity: StudentJpaEntity?,

    override val createdAt: LocalDateTime

) : BaseEntity()