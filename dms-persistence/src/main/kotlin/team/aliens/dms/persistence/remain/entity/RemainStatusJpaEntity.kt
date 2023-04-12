package team.aliens.dms.persistence.remain.entity

import team.aliens.dms.persistence.BaseTimeEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

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
