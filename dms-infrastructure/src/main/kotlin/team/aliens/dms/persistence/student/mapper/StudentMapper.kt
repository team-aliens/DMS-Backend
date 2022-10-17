package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId
import team.aliens.dms.persistence.room.repository.RoomJpaRepository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class StudentMapper(
    private val roomJpaRepository: RoomJpaRepository,
    private val userJpaRepository: UserJpaRepository
) : GenericMapper<Student, StudentJpaEntity> {

    override fun toDomain(entity: StudentJpaEntity?): Student? {
        return Student(
            studentId = entity!!.studentId,
            roomNumber = entity.room!!.id.roomNumber,
            schoolId = entity.studentId,
            grade = entity.grade,
            classRoom = entity.classRoom,
            number = entity.number
        )
    }

    override fun toEntity(domain: Student): StudentJpaEntity {
        val user = userJpaRepository.findByIdOrNull(domain.studentId)!!

        val room =  roomJpaRepository.findByIdOrNull(
            RoomJpaEntityId(domain.roomNumber, domain.studentId)
        )!!

        return StudentJpaEntity(
            studentId = domain.studentId,
            user = user,
            room = room,
            grade = domain.grade,
            classRoom = domain.classRoom,
            number = domain.number
        )
    }
}