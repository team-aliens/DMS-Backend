package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.repository.RoomJpaRepository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class StudentMapper(
    private val roomRepository: RoomJpaRepository,
    private val userRepository: UserJpaRepository
) : GenericMapper<Student, StudentJpaEntity> {

    override fun toDomain(entity: StudentJpaEntity?): Student? {
        return entity?.let {
            Student(
                id = it.id,
                roomId = it.room!!.id!!,
                roomNumber = it.room!!.number,
                schoolId = it.user!!.school!!.id!!,
                grade = it.grade,
                classRoom = it.classRoom,
                number = it.number,
                name = it.name,
                profileImageUrl = it.profileImageUrl
            )
        }
    }

    override fun toEntity(domain: Student): StudentJpaEntity {
        val user = userRepository.findByIdOrNull(domain.id)
        val room = roomRepository.findByIdOrNull(domain.roomId)

        return StudentJpaEntity(
            id = domain.id,
            user = user,
            room = room,
            grade = domain.grade,
            classRoom = domain.classRoom,
            number = domain.number,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl!!
        )
    }
}