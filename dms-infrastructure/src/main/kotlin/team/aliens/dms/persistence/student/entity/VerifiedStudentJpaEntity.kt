package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "tbl_verified_student")
class VerifiedStudentJpaEntity(

    @EmbeddedId
    val id: VerifiedStudentJpaEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val roomNumber: Int

)

@Embeddable
data class VerifiedStudentJpaEntityId(

    @Column(columnDefinition = "VARCHAR(5)", nullable = false)
    val gcn: String,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable