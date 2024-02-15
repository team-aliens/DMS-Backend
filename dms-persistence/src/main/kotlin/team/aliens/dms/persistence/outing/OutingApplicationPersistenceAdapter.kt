package team.aliens.dms.persistence.outing

import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.spi.OutingApplicationPort
import team.aliens.dms.persistence.outing.mapper.OutingApplicationMapper
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import java.time.LocalDate
import java.util.UUID

@Component
class OutingApplicationPersistenceAdapter(
    private val outingApplicationMapper: OutingApplicationMapper,
    private val outingApplicationRepository: OutingApplicationJpaRepository
) : OutingApplicationPort {

    override fun queryOutingApplicationById(outingApplicationId: UUID) = outingApplicationMapper.toDomain(
        outingApplicationRepository.findByIdOrNull(outingApplicationId)
    )

    override fun existOutingApplicationByOutAtAndStudentId(outAt: LocalDate, studentId: UUID) =
        outingApplicationRepository.existsByOutAtAndStudentId(outAt, studentId)

    override fun saveOutingApplication(outingApplication: OutingApplication) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.save(
                outingApplicationMapper.toEntity(outingApplication)
            )
        )!!
}
