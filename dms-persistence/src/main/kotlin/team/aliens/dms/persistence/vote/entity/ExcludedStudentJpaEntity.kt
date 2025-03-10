package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.util.UUID

@Entity
@Table(name = "tbl_excluded_student")
class ExcludedStudentJpaEntity(

    @Id
    @Column(name = "student_id")
    val studentId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?
)
