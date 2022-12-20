package team.aliens.dms.persistence.studyroom.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import team.aliens.dms.persistence.BaseUUIDEntity

@Entity
@Table(name = "tbl_seat_type")
class SeatTypeJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String,

    @Column(columnDefinition = "CHAR(7)", nullable = false)
    val color: String

) : BaseUUIDEntity(id)