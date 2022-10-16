package team.aliens.dms.persistence.room.entity

import team.aliens.dms.persistence.school.entity.SchoolEntity
import javax.persistence.*

@Entity
@Table(name = "tbl_room")
class RoomEntity(

    @EmbeddedId
    val id: RoomEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity
)