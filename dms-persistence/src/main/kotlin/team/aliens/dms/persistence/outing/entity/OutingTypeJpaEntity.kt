package team.aliens.dms.persistence.outing.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.UUID
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Table
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn

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
