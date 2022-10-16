package team.aliens.dms.persistence.point.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tbl_point_option")
class PointOptionEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT", nullable = false)
    val score: Int

) : BaseUUIDEntity()