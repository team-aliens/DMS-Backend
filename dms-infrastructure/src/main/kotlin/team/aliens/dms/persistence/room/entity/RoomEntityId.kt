package team.aliens.dms.persistence.room.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class RoomEntityId(

    @Column(columnDefinition = "UNSIGNED INT", nullable = false)
    val roomNumber: Int,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable