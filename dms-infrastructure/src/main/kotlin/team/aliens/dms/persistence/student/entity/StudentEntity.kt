package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.room.entity.RoomEntity
import team.aliens.dms.persistence.school.entity.SchoolEntity
import team.aliens.dms.persistence.user.entity.UserEntity
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "tbl_student",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("grade", "class_room", "number"))
    ]
)
class StudentEntity(

    @Id
    val studentId: UUID,

    @MapsId("studentId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val userEntity: UserEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "room_number"),
        JoinColumn(name = "school_id")
    )
    val roomEntity: RoomEntity?,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val grade: Int,

    @Column(name = "class_room", columnDefinition = "TINYINT", nullable = false)
    val classRoom: Int,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val number: Int
)