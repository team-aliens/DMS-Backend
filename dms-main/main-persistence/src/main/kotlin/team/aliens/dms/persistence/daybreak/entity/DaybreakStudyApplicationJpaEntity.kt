package team.aliens.dms.persistence.daybreak.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.teacher.entity.TeacherJpaEntity
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tbl_daybreak_study_application")
class DaybreakStudyApplicationJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "VARCHAR(1000)", nullable = false)
    val reason: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    var status: Status,

    @Column(columnDefinition = "DATE", nullable = false)
    val startDate: LocalDate,

    @Column(columnDefinition = "DATE", nullable = false)
    val endDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    val studentJpaEntity: StudentJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val schoolJpaEntity: SchoolJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    val daybreakStudyTypeJpaEntity: DaybreakStudyTypeJpaEntity,

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    val teacherJpaEntity: TeacherJpaEntity,

): BaseEntity(id)