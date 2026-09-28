package team.aliens.dms.persistence.remain.entity

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
@Table(name = "tbl_remain_option")
class RemainOptionJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(length = 100, nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String

) : BaseUUIDEntity(id)
