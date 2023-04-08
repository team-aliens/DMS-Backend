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
                roomLocation = it.roomLocation,
                schoolId = it.user!!.school!!.id!!,
                grade = it.grade,
                classRoom = it.classRoom,
                number = it.number,
                name = it.name,
                profileImageUrl = it.profileImageUrl,
                sex = it.sex,
                deletedAt = it.deletedAt
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
            roomLocation = domain.roomLocation,
            grade = domain.grade,
            classRoom = domain.classRoom,
            number = domain.number,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl!!,
            sex = domain.sex,
            deletedAt = domain.deletedAt
        )
    }
}
