package team.aliens.dms.persistence.studyroom.entity

import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

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
