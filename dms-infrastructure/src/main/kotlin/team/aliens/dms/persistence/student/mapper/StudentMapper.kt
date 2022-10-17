package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomEntityId
import team.aliens.dms.persistence.room.repository.RoomRepository
import team.aliens.dms.persistence.student.entity.StudentEntity
import team.aliens.dms.persistence.user.repository.UserRepository

@Component
class StudentMapper(
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository
) : GenericMapper<Student, StudentEntity> {

    override fun toDomain(e: StudentEntity): Student {
        val room = e.roomEntity?.let {
            roomRepository.findByIdOrNull(it.id)
        } ?: throw RuntimeException()

        return Student(
            studentId = e.studentId,
            grade = e.grade,
            classRoom = e.classRoom,
            studentNumber = e.studentNumber,
            roomNumber = room.id.roomNumber,
            schoolId = room.id.schoolId
        )
    }

    override fun toEntity(d: Student): StudentEntity {
        val user = userRepository.findByIdOrNull(d.studentId) ?: throw RuntimeException()
        val room =  roomRepository.findByIdOrNull(RoomEntityId(d.roomNumber, d.studentId)) ?: throw RuntimeException()

        return StudentEntity(
            studentId = d.studentId,
            grade = d.grade,
            classRoom = d.classRoom,
            studentNumber = d.studentNumber,
            userEntity = user,
            roomEntity = room
        )
    }
}