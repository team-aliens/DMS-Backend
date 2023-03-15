package team.aliens.dms.persistence.student.entity

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
@Table(name = "tbl_student_tag")
class StudentTagJpaEntity(

    @EmbeddedId
    val id: Id,

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", columnDefinition = "BINARY(16)", nullable = false)
    val tag: TagJpaEntity?

) {

    @Embeddable
    data class Id(
        @Column(columnDefinition = "BINARY(16)", nullable = false)
        val studentId: UUID,

        @Column(columnDefinition = "BINARY(16)", nullable = false)
        val tagId: UUID
    ) : Serializable
}