package team.aliens.dms.persistence.meal.entity

import team.aliens.dms.persistence.school.entity.SchoolEntity
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "tbl_meal")
class MealEntity(

    @EmbeddedId
    val id: MealEntityId,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity?,

    @Column(columnDefinition = "VARCHAR(255)")
    val breakfast: String?,

    @Column(columnDefinition = "VARCHAR(255)")
    val lunch: String?,

    @Column(columnDefinition = "VARCHAR(255)")
    val dinner: String?
)

@Embeddable
data class MealEntityId(

    @Column(columnDefinition = "DATE", nullable = false)
    val mealDate: LocalDate,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable