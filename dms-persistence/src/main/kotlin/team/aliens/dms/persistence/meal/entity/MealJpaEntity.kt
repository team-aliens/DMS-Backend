package team.aliens.dms.persistence.meal.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.time.LocalDate
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
@Table(name = "tbl_meal")
class MealJpaEntity(

    @EmbeddedId
    val id: MealJpaEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(255)")
    val breakfast: String?,

    @Column(columnDefinition = "VARCHAR(255)")
    val lunch: String?,

    @Column(columnDefinition = "VARCHAR(255)")
    val dinner: String?

)

@Embeddable
data class MealJpaEntityId(

    @Column(columnDefinition = "DATE", nullable = false)
    val mealDate: LocalDate,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable
