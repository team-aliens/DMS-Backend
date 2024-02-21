package team.aliens.dms.persistence.student

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Expression
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.StudentPort
import team.aliens.dms.domain.student.spi.vo.AllStudentsVO
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.room.entity.QRoomJpaEntity.roomJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.student.mapper.StudentMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.student.repository.vo.QQueryAllStudentsVO
import team.aliens.dms.persistence.student.repository.vo.QQueryStudentWithPointVO
import team.aliens.dms.persistence.student.repository.vo.QQueryStudentsWithTagVO
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity
import team.aliens.dms.persistence.tag.entity.QTagJpaEntity.tagJpaEntity
import team.aliens.dms.persistence.tag.mapper.TagMapper
import java.util.UUID

@Component
class StudentPersistenceAdapter(
    private val studentMapper: StudentMapper,
    private val tagMapper: TagMapper,
    private val studentRepository: StudentJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : StudentPort {

    override fun queryStudentBySchoolIdAndGcn(
        schoolId: UUID,
        grade: Int,
        classRoom: Int,
        number: Int,
    ) = studentMapper.toDomain(
        studentRepository.findByRoomSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
    )

    override fun queryStudentById(studentId: UUID) = studentMapper.toDomain(
        studentRepository.findByIdOrNull(studentId)
    )

    override fun queryStudentByUserId(userId: UUID) = studentMapper.toDomain(
        studentRepository.findByUserId(userId)
    )

    override fun existsStudentByUserId(studentId: UUID) =
        studentRepository.existsByUserId(studentId)

    override fun existsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): Boolean {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .where(
                roomJpaEntity.school.id.eq(schoolId),
                Expressions.list(studentJpaEntity.grade, studentJpaEntity.classRoom, studentJpaEntity.number)
                    .`in`(*queryStudentGcnIn(gcnList))
            )
            .fetchFirst() != null
    }

    private fun queryStudentGcnIn(gcnList: List<Triple<Int, Int, Int>>): Array<Expression<Tuple>> {
        val tuple: MutableList<Expression<Tuple>> = ArrayList()
        for (gcn in gcnList) {
            tuple.add(
                Expressions.template(
                    Tuple::class.java,
                    "(({0}, {1}, {2}))",
                    gcn.first, gcn.second, gcn.third
                )
            )
        }

        return tuple.toTypedArray()
    }

    override fun saveStudent(student: Student) = studentMapper.toDomain(
        studentRepository.save(
            studentMapper.toEntity(student)
        )
    )!!

    override fun saveAllStudent(students: List<Student>) {
        studentRepository.saveAll(
            students.map {
                studentMapper.toEntity(it)
            }
        )
    }

    override fun queryBySchoolIdAndGcnIn(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity).fetchJoin()
            .where(
                roomJpaEntity.school.id.eq(schoolId),
                Expressions.list(studentJpaEntity.grade, studentJpaEntity.classRoom, studentJpaEntity.number)
                    .`in`(*queryStudentGcnIn(gcnList))
            )
            .fetch()
            .mapNotNull { studentMapper.toDomain(it) }
    }

    override fun queryBySchoolIdAndRoomNumberAndRoomLocationIn(
        schoolId: UUID,
        roomNumberLocations: List<Pair<String, String>>
    ): List<Student> {

        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity).fetchJoin()
            .where(
                roomJpaEntity.school.id.eq(schoolId),
                Expressions.list(roomJpaEntity.number, studentJpaEntity.roomLocation)
                    .`in`(*queryRoomNumberAndRoomLocationIn(roomNumberLocations))
            )
            .fetch()
            .mapNotNull { studentMapper.toDomain(it) }
    }

    private fun queryRoomNumberAndRoomLocationIn(roomNumberLocations: List<Pair<String, String>>): Array<Expression<Tuple>> {
        val tuple: MutableList<Expression<Tuple>> = ArrayList()
        for (roomNumberLocation in roomNumberLocations) {
            tuple.add(
                Expressions.template(
                    Tuple::class.java,
                    "(({0}, {1}))",
                    roomNumberLocation.first, roomNumberLocation.second
                )
            )
        }

        return tuple.toTypedArray()
    }

    override fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): List<StudentWithTag> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .leftJoin(studentTagJpaEntity).on(studentJpaEntity.id.eq(studentTagJpaEntity.student.id)).fetchJoin()
            .leftJoin(tagJpaEntity).on(studentTagJpaEntity.tag.id.eq(tagJpaEntity.id))
            .leftJoin(pointHistoryJpaEntity).on(eqStudentRecentPointHistory())
            .where(
                roomJpaEntity.school.id.eq(schoolId),
                nameContains(name),
                pointTotalBetween(pointFilter),
                hasAllTags(tagIds)
            )
            .orderBy(
                sortFilter(sort),
                studentJpaEntity.grade.asc(),
                studentJpaEntity.classRoom.asc(),
                studentJpaEntity.number.asc()
            )
            .transform(
                groupBy(studentJpaEntity.id)
                    .list(
                        QQueryStudentsWithTagVO(
                            studentJpaEntity.id,
                            studentJpaEntity.name,
                            studentJpaEntity.grade,
                            studentJpaEntity.classRoom,
                            studentJpaEntity.number,
                            roomJpaEntity.number,
                            studentJpaEntity.profileImageUrl,
                            studentJpaEntity.sex,
                            list(tagJpaEntity)
                        )
                    )
            )
            .map {
                StudentWithTag(
                    id = it.id,
                    name = it.name,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    roomNumber = it.roomNumber,
                    profileImageUrl = it.profileImageUrl,
                    sex = it.sex,
                    tags = it.tags
                        .map { tag ->
                            tagMapper.toDomain(tag)!!
                        }
                )
            }
    }

    private fun nameContains(name: String?) = name?.run { studentJpaEntity.name.contains(this) }

    private fun pointTotalBetween(pointFilter: PointFilter): BooleanExpression? {
        if (pointFilter.filterType == null) {
            return null
        }

        return when (pointFilter.filterType) {
            PointFilterType.BONUS -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.bonusTotal)
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }

            PointFilterType.MINUS -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.minusTotal)
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }

            else -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.bonusTotal.subtract(pointHistoryJpaEntity.minusTotal))
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }
        }
    }

    private fun hasAllTags(tagIds: List<UUID>?): BooleanExpression? =
        tagIds?.run {
            studentJpaEntity.id.`in`(
                select(studentTagJpaEntity.student.id)
                    .from(studentTagJpaEntity)
                    .where(studentTagJpaEntity.tag.id.`in`(tagIds))
                    .groupBy(studentTagJpaEntity.student)
                    .having(studentTagJpaEntity.count().eq(tagIds.size.toLong()))
            )
        }

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

    override fun queryStudentsByRoomNumberAndSchoolId(roomNumber: String, schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity).fetchJoin()
            .where(
                roomJpaEntity.number.eq(roomNumber),
                roomJpaEntity.school.id.eq(schoolId)
            ).fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    override fun queryStudentsBySchoolId(schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity).fetchJoin()
            .where(
                roomJpaEntity.school.id.eq(schoolId)
            )
            .orderBy(roomJpaEntity.number.asc())
            .fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    override fun queryStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO> {
        return queryFactory
            .select(
                QQueryStudentWithPointVO(
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    pointHistoryJpaEntity.bonusTotal,
                    pointHistoryJpaEntity.minusTotal
                )
            )
            .from(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .leftJoin(pointHistoryJpaEntity)
            .on(eqStudentRecentPointHistory())
            .where(
                studentJpaEntity.id.`in`(studentIds)
            )
            .fetch()
            .map {
                StudentWithPointVO(
                    name = it.name,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    bonusTotal = it.bonusTotal ?: 0,
                    minusTotal = it.minusTotal ?: 0
                )
            }
    }

    private fun eqStudentRecentPointHistory(): BooleanExpression? {
        return pointHistoryJpaEntity.studentName.eq(studentJpaEntity.name)
            .and(eqGcn())
            .and(
                pointHistoryJpaEntity.createdAt.eq(
                    select(pointHistoryJpaEntity.createdAt.max())
                        .from(pointHistoryJpaEntity)
                        .where(
                            pointHistoryJpaEntity.school.id.eq(roomJpaEntity.school.id),
                            pointHistoryJpaEntity.studentName.eq(studentJpaEntity.name),
                            eqGcn()
                        )
                )
            )
    }

    private fun eqGcn(): BooleanBuilder {
        val condition = BooleanBuilder()

        val gcn = pointHistoryJpaEntity.studentGcn
        condition.and(
            gcn.substring(0, 1).eq(studentJpaEntity.grade.stringValue())
        ).and(
            gcn.substring(1, 2).endsWith(studentJpaEntity.classRoom.stringValue())
        ).and(
            gcn.substring(2).endsWith(studentJpaEntity.number.stringValue())
        )

        return condition
    }

    override fun queryAllStudentsByIdsIn(studentIds: List<UUID>) =
        studentRepository.findAllByIdIn(studentIds)
            .map {
                studentMapper.toDomain(it)!!
            }

    override fun queryAllStudentsByName(name: String?): List<AllStudentsVO> {
        return queryFactory
            .select(
                QQueryAllStudentsVO(
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    studentJpaEntity.profileImageUrl,
                )
            )
            .from(studentJpaEntity)
            .where(name?.let { studentJpaEntity.name.contains(it) })
            .fetch()
    }
}
