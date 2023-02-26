package team.aliens.dms.persistence.school.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

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
    val remainService: Boolean

)
