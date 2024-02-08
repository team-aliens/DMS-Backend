package team.aliens.dms.persistence.outing.entity

import team.aliens.dms.persistence.student.entity.StudentJpaEntity
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
@Table(name = "tbl_companion_for_outing")
class CompanionForOutingJpaEntity(

    @EmbeddedId
    val id: CompanionForOutingJpaEntityId,

    @MapsId("outingApplicationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outing_application_id", columnDefinition = "BINARY(16)", nullable = false)
    val outingApplication: OutingApplicationJpaEntity?,


    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?

)

@Embeddable
data class CompanionForOutingJpaEntityId(

    @Column
    val outingApplicationId: UUID,

    @Column
    val studentId: UUID,

) : Serializable
