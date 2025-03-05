package team.aliens.dms.persistence.school.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "tbl_available_feature")
class AvailableFeatureJpaEntity(

    @Id
    val schoolId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val mealService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val noticeService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val pointService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val studyRoomService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val remainService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val outingService: Boolean,

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    val volunteerService: Boolean

)
