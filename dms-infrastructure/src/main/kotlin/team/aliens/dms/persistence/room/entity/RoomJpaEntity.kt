package team.aliens.dms.persistence.room.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_room")
class RoomJpaEntity(

    override val id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val number: Int

) : BaseUUIDEntity()