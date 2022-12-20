package team.aliens.dms.persistence.studyroom.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Entity
@Table(
    name = "tbl_study_room",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("floor", "name"))
    ]
)
class StudyRoomJpaEntity(

    override val id: UUID?,

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
    val inUseHeadcount: Int,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val availableHeadcount: Int,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val availableSex: Sex,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val availableGrade: Int,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    @Transient
    val eastDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val westDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val southDescription: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val northDescription: String

) : BaseUUIDEntity(id)