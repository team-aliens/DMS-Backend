package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.*
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.util.*

@Entity
@Table(name = "tbl_excluded_student")
class ExcludedStudentJpaEntity(

    @Id
    @Column(name = "student_id")
    val id: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?
)
