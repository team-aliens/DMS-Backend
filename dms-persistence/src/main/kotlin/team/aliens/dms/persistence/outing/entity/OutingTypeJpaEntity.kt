package team.aliens.dms.persistence.outing.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Table
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn

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
    val title: String,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable
