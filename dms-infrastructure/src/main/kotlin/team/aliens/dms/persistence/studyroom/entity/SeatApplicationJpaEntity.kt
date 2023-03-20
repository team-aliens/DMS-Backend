package team.aliens.dms.persistence.studyroom.entity

import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "tbl_seat_application")
class SeatApplicationJpaEntity(

    @EmbeddedId
    val id: SeatApplicationJpaEntityId,

    @MapsId("seatId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", columnDefinition = "BINARY(16)", nullable = false)
    val seat: SeatJpaEntity?,

    @MapsId("timeSlotId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = false)
    val timeSlot: TimeSlotJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

)

@Embeddable
data class SeatApplicationJpaEntityId(

    @Column
    val seatId: UUID,

    @Column
    val timeSlotId: UUID

) : Serializable
