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
import javax.persistence.OneToOne
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
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = true)
    val timeSlot: StudyRoomTimeSlotJpaEntity?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = true)
    val student: StudentJpaEntity?,

)

@Embeddable
data class SeatApplicationJpaEntityId(

    @JoinColumn(nullable = false)
    val seatId: UUID,

    @Column(nullable = true)
    val timeSlotId: UUID?

) : Serializable
