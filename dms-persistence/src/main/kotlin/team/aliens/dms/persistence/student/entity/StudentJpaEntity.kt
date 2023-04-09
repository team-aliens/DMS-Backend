package team.aliens.dms.persistence.student.entity

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import org.hibernate.annotations.Where
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Entity
@Table(name = "tbl_student")
@Where(clause = "deleted_at is null")
class StudentJpaEntity(

    override val id: UUID?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = true)
    val user: UserJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", columnDefinition = "BINARY(16)", nullable = false)
    val room: RoomJpaEntity?,

    @Column(columnDefinition = "CHAR(1)", nullable = false)
    val roomLocation: String,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val grade: Int,

    @Column(name = "class_room", columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val classRoom: Int,

    @Column(columnDefinition = "TINYINT UNSIGNED", nullable = false)
    val number: Int,

    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    val name: String,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val profileImageUrl: String,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    @Column(columnDefinition = "DATETIME")
    val deletedAt: LocalDateTime?
) : BaseUUIDEntity(id)
