package team.aliens.dms.persistence.meal.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "tbl_meal")
class MealJpaEntity(

    @EmbeddedId
    val id: MealJpaEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    val breakfast: String?,

    val lunch: String?,

    val dinner: String?

)

@Embeddable
data class MealJpaEntityId(

    @Column(nullable = false)
    val mealDate: LocalDate,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable
