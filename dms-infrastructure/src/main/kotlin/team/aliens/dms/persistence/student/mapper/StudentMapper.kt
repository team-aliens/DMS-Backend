package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId
import team.aliens.dms.persistence.room.repository.RoomRepository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.user.repository.UserRepository

@Component
class StudentMapper(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : GenericMapper<Student, StudentJpaEntity> {

    override fun toDomain(e: StudentJpaEntity): Student {
        val room = e.roomJpaEntity?.let {
            roomRepository.findByIdOrNull(it.id)
        } ?: throw RuntimeException()

        return Student(
            studentId = e.studentId,
            roomNumber = room.id.roomNumber,
            schoolId = room.id.schoolId,
            grade = e.grade,
            classRoom = e.classRoom,
            number = e.number
        )
    }

    override fun toEntity(d: Student): StudentJpaEntity {
        val user = userRepository.findByIdOrNull(d.studentId) ?: throw RuntimeException()
        val room =  roomRepository.findByIdOrNull(RoomJpaEntityId(d.roomNumber, d.studentId)) ?: throw RuntimeException()

        return StudentJpaEntity(
            studentId = d.studentId,
            userJpaEntity = user,
            roomJpaEntity = room,
            grade = d.grade,
            classRoom = d.classRoom,
            number = d.number
        )
    }
}