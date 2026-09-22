package team.aliens.dms.persistence.student.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.SQLRestriction
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_student")
@SQLRestriction("deleted_at is null")
class StudentJpaEntity(

    id: UUID?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id", nullable = true)
    val user: UserJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    val room: RoomJpaEntity?,

    @Column(length = 1, nullable = false)
    val roomLocation: String,

    @Column(nullable = false)
    val grade: Int,

    @Column(name = "class_room", nullable = false)
    val classRoom: Int,

    @Column(nullable = false)
    val number: Int,

    @Column(length = 10, nullable = false)
    val name: String,

    @Column(length = 500, nullable = false)
    val profileImageUrl: String,

    @Column(length = 6, nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    val deletedAt: LocalDateTime?,
) : BaseUUIDEntity(id)
