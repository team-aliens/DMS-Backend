package team.aliens.dms.persistence.school.entity

import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.model.CustomDayOfWeek
import java.io.Serializable
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_application_available_time")
class ApplicationAvailableTimeJpaEntity(

    @EmbeddedId
    val id: ApplicationAvailableTimeId,

    @MapsId("schoolId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    val startDayOfWeek: CustomDayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    val endDayOfWeek: CustomDayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime
)

@Embeddable
data class ApplicationAvailableTimeId(

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val type: ApplicationAvailableTimeType,

    @Column(nullable = false, name = "school_id")
    val schoolId: UUID

) : Serializable
