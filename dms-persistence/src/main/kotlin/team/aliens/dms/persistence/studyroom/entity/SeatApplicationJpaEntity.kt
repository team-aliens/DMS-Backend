package team.aliens.dms.persistence.studyroom.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.io.Serializable
import java.util.UUID

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
