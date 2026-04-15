package team.aliens.dms.persistence.daybreak.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID

@Entity
@Table(name = "tbl_daybreak_study_type")
class DaybreakStudyTypeJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val schoolJpaEntity: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String

) : BaseUUIDEntity(id)
