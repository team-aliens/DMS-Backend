package team.aliens.dms.persistence.meal.entity

import java.io.Serializable
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MealEntityId(

    @Column(columnDefinition = "DATE", nullable = false)
    val mealDate: LocalDate,

    @Column(nullable = false)
    val schoolId: UUID

) : Serializable