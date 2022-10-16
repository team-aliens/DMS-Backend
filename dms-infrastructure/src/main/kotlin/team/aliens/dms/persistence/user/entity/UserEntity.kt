package team.aliens.dms.persistence.user.entity

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import team.aliens.dms.global.DefaultImage
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.*

@DynamicInsert
@Entity
@Table(name = "tbl_user")
class UserEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    val accountId: String,

    @Column(columnDefinition = "CHAR(60)", nullable = false)
    val password: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    val email: String,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @ColumnDefault(DefaultImage.PROFILE_IMAGE_URL)
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val profileImageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity,

    override val createdAt: LocalDateTime,

    @Column(columnDefinition = "DATETIME")
    val deletedAt: LocalDateTime

) : BaseEntity()