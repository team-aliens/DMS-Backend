package team.aliens.dms.persistence.tag.entity

import team.aliens.dms.persistence.BaseTimeEntity
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table
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
