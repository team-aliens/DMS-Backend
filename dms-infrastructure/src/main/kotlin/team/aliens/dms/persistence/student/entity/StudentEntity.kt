package team.aliens.dms.persistence.student.entity

import team.aliens.dms.persistence.room.entity.RoomEntity
import team.aliens.dms.persistence.school.entity.SchoolEntity
import team.aliens.dms.persistence.user.entity.UserEntity
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "tbl_student",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["grade", "class_room", "number"])
    ]
)
class StudentEntity(

    @Id
    val studentId: UUID,

    @Column(columnDefinition = "TINYINT")
    val grade: Int,

    @Column(columnDefinition = "TINYINT")
    val classRoom: Int,

    @Column(columnDefinition = "TINYINT")
    val studentNumber: Int,

    @MapsId("studentId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)")
    val userEntity: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    val roomEntity: RoomEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)")
    val schoolEntity: SchoolEntity
)