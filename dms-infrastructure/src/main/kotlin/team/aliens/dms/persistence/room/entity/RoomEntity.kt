package team.aliens.dms.persistence.room.entity

import team.aliens.dms.persistence.school.entity.SchoolEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_room")
class RoomEntity(

    @EmbeddedId
    val id: RoomEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity?
)

@Embeddable
data class RoomEntityId(

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val roomNumber: Int,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable