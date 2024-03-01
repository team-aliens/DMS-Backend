package team.aliens.dms.persistence.tag.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseTimeEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity

@Entity
@Table(name = "tbl_student_tag")
class StudentTagJpaEntity(

    @EmbeddedId
    val id: StudentTagId,

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", columnDefinition = "BINARY(16)", nullable = false)
    val tag: TagJpaEntity?

) : BaseTimeEntity()
