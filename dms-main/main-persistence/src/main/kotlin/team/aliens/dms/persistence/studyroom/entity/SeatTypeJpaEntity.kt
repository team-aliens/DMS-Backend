package team.aliens.dms.persistence.studyroom.entity

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
@Table(name = "tbl_seat_type")
class SeatTypeJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String,

    @Column(columnDefinition = "CHAR(7)", nullable = false)
    val color: String

) : BaseUUIDEntity(id)
