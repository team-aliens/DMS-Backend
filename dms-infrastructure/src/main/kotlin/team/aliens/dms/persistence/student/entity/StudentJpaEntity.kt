package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "tbl_student",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("grade", "class_room", "number"))
    ]
)
class StudentJpaEntity(

    @Id
    val studentId: UUID,

    @MapsId("studentId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val userJpaEntity: UserJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "room_number", nullable = false),
        JoinColumn(name = "school_id", nullable = false)
    )
    val roomJpaEntity: RoomJpaEntity?,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val grade: Int,

    @Column(name = "class_room", columnDefinition = "TINYINT", nullable = false)
    val classRoom: Int,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val number: Int
)