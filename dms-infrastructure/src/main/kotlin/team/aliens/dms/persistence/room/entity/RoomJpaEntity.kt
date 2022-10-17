package team.aliens.dms.persistence.room.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_room")
class RoomJpaEntity(

    @EmbeddedId
    val id: RoomJpaEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val schoolJpaEntity: SchoolJpaEntity?
)

@Embeddable
data class RoomJpaEntityId(

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val roomNumber: Int,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable