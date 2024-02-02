package team.aliens.dms.persistence.outing.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_outing_type")
class OutingTypeJpaEntity(

    @EmbeddedId
    val id: OutingTypeJpaEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    )

@Embeddable
data class OutingTypeJpaEntityId(

    @Column(columnDefinition = "VARCHAR(20)" ,nullable = false)
    val outingType: String,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable
