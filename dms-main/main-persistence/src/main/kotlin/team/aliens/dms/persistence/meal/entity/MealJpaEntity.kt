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
