package team.aliens.dms.persistence.studyroom.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID

@Entity
@Table(
    name = "tbl_study_room",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "floor", "name"))
    ]
)
class StudyRoomJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val floor: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val widthSize: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val heightSize: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val availableHeadcount: Int,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val availableSex: Sex,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val availableGrade: Int,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val eastDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val westDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val southDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val northDescription: String

) : BaseUUIDEntity(id)
