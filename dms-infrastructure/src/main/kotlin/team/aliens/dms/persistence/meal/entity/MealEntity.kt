package team.aliens.dms.persistence.meal.entity

import team.aliens.dms.persistence.school.entity.SchoolEntity
import javax.persistence.*

@Entity
@Table(name = "tbl_meal")
class MealEntity(

    @EmbeddedId
    val id: MealEntityId,

    @Column(columnDefinition = "VARCHAR(255)")
    val breakfast: String,

    @Column(columnDefinition = "VARCHAR(255)")
    val lunch: String,

    @Column(columnDefinition = "VARCHAR(255)")
    val dinner: String,

    @MapsId("schoolId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity
)