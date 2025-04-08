package team.aliens.dms.persistence.vote

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.ExcludedStudent
import team.aliens.dms.domain.vote.spi.ExcludedStudentPort
import team.aliens.dms.persistence.vote.mapper.ExcludedStudentMapper
import team.aliens.dms.persistence.vote.repository.ExcludedStudentJpaRepository
import java.util.UUID

@Component
class ExcludedStudentPersistenceAdapter(
    val excludedStudentJpaRepository: ExcludedStudentJpaRepository,
    val excludedStudentMapper: ExcludedStudentMapper,
    val queryFactory: JPAQueryFactory
) : ExcludedStudentPort {

    override fun saveExcludedStudent(excludedStudent: ExcludedStudent) = excludedStudentMapper.toDomain(
        excludedStudentJpaRepository.save(
            excludedStudentMapper.toEntity(excludedStudent)
        )
    )!!

    override fun deleteExcludedStudentById(excludedStudentId: UUID) {
        excludedStudentJpaRepository.deleteById(excludedStudentId)
    }

    override fun existExcludedStudentById(excludedStudentId: UUID): Boolean =
        excludedStudentJpaRepository.existsById(excludedStudentId)

    override fun queryAllExcludedStudents() = excludedStudentJpaRepository.findAll().map {
        excludedStudentMapper.toDomain(it)!!
    }

    override fun queryExcludedStudentById(excludedStudentId: UUID): ExcludedStudent? =
        excludedStudentMapper.toDomain(excludedStudentJpaRepository.findByIdOrNull(excludedStudentId))
}
