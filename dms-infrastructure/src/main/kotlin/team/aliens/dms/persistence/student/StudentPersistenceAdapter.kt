package team.aliens.dms.persistence.student

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.StudentPort
import team.aliens.dms.persistence.room.entity.QRoomJpaEntity.roomJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.student.mapper.StudentMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity
import java.util.UUID
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.persistence.student.mapper.VerifiedStudentMapper
import team.aliens.dms.persistence.student.repository.VerifiedStudentJpaRepository

@Component
class StudentPersistenceAdapter(
    private val studentMapper: StudentMapper,
    private val studentRepository: StudentJpaRepository,
    private val verifiedStudentRepository: VerifiedStudentJpaRepository,
    private val verifiedStudentMapper: VerifiedStudentMapper,
    private val queryFactory: JPAQueryFactory
) : StudentPort {

    override fun queryStudentBySchoolIdAndGcn(
        schoolId: UUID,
        grade: Int,
        classRoom: Int,
        number: Int
    ) = studentMapper.toDomain(
        studentRepository.findByUserSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
    )

    override fun queryStudentById(studentId: UUID) = studentMapper.toDomain(
        studentRepository.findByIdOrNull(studentId)
    )

    override fun saveStudent(student: Student) = studentMapper.toDomain(
        studentRepository.save(
            studentMapper.toEntity(student)
        )
    )!!

    override fun saveAllVerifiedStudent(verifiedStudents: List<VerifiedStudent>) {
        verifiedStudentRepository.saveAll(
            verifiedStudents.map {
                verifiedStudentMapper.toEntity(it)
            }
        )
    }

    override fun queryStudentsByNameAndSort(name: String?, sort: Sort, schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .where(
                nameContains(name),
                schoolEq(schoolId)
            )
            .orderBy(
                sortFilter(sort),
                studentJpaEntity.grade.asc(),
                studentJpaEntity.classRoom.asc(),
                studentJpaEntity.number.asc()
            )
            .fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    private fun nameContains(name: String?) = name?.run { studentJpaEntity.name.contains(this) }

    private fun schoolEq(schoolId: UUID) = userJpaEntity.school.id.eq(schoolId)

    private fun sortFilter(sort: Sort): OrderSpecifier<*>? {
        return when (sort) {
            Sort.NAME -> {
                studentJpaEntity.name.asc()
            }
            else -> {
                studentJpaEntity.grade.asc()
            }
        }
    }

    override fun queryUserByRoomNumberAndSchoolId(roomNumber: Int, schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .where(
                roomJpaEntity.number.eq(roomNumber),
                userJpaEntity.school.id.eq(schoolId)
            ).fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    override fun deleteStudent(student: Student) {
        studentRepository.delete(
            studentMapper.toEntity(student)
        )
    }
}