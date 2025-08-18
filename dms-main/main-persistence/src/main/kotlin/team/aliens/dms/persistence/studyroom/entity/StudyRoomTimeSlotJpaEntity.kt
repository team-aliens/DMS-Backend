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
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "tbl_study_room_time_slot")
class StudyRoomTimeSlotJpaEntity(

    @EmbeddedId
    val id: StudyRoomTimeSlotJpaEntityId,

    @MapsId("studyRoomId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id", columnDefinition = "BINARY(16)", nullable = false)
    val studyRoom: StudyRoomJpaEntity?,

    @MapsId("timeSlotId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_id", columnDefinition = "BINARY(16)", nullable = false)
    val timeSlot: TimeSlotJpaEntity?,

)

@Embeddable
data class StudyRoomTimeSlotJpaEntityId(

    @Column
    val studyRoomId: UUID,

    @Column
    val timeSlotId: UUID,

) : Serializable
