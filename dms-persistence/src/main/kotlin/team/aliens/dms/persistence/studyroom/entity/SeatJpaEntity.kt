package team.aliens.dms.persistence.studyroom.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID

@Entity
@Table(
    name = "tbl_seat",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("study_room_id", "seat_type_id", "number")),
        UniqueConstraint(columnNames = arrayOf("study_room_id", "width_location", "height_location"))
    ]
)
class SeatJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id", columnDefinition = "BINARY(16)", nullable = false)
    val studyRoom: StudyRoomJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", columnDefinition = "BINARY(16)")
    val type: SeatTypeJpaEntity?,

    @Column(name = "width_location", columnDefinition = "INT UNSIGNED", nullable = false)
    val widthLocation: Int,

    @Column(name = "height_location", columnDefinition = "INT UNSIGNED", nullable = false)
    val heightLocation: Int,

    @Column(columnDefinition = "INT UNSIGNED")
    val number: Int?,

    @Column(columnDefinition = "VARCHAR(11)", nullable = false)
    @Enumerated(EnumType.STRING)
    val status: SeatStatus

) : BaseUUIDEntity(id)
