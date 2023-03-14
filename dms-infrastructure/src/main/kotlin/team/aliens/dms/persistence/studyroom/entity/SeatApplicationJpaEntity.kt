package team.aliens.dms.persistence.studyroom.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "tbl_seat_application",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("seat_id", "time_slot_id"))
    ]
)
class SeatApplicationJpaEntity(

    override val id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", columnDefinition = "BINARY(16)", nullable = false)
    val seat: SeatJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = true)
    val timeSlot: StudyRoomTimeSlotJpaEntity?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

) : BaseUUIDEntity(id)
