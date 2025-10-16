package team.aliens.dms.persistence.remain.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseTimeEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_remain_status")
class RemainStatusJpaEntity(

    @Id
    @Column(name = "student_id")
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remain_option_id", columnDefinition = "BINARY(16)", nullable = false)
    val remainOption: RemainOptionJpaEntity?,

    override val createdAt: LocalDateTime

) : BaseTimeEntity()
