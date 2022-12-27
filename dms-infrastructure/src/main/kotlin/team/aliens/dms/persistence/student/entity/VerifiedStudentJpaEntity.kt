package team.aliens.dms.persistence.student.entity

import java.util.UUID
import team.aliens.dms.domain.student.model.Sex
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table
import team.aliens.dms.persistence.BaseUUIDEntity

@Entity
@Table(name = "tbl_verified_student")
class VerifiedStudentJpaEntity(

    override val id: UUID?,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val schoolName: String,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String,

    @Column(columnDefinition = "INT UNSIGNED", nullable = false)
    val roomNumber: Int,

    @Column(columnDefinition = "VARCHAR(4)", nullable = false)
    val gcn: String,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex

) : BaseUUIDEntity(id)