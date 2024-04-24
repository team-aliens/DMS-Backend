package team.aliens.dms.persistence.bug

import org.springframework.stereotype.Component
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.spi.BugPort
import team.aliens.dms.persistence.bug.mapper.BugReportMapper
import team.aliens.dms.persistence.bug.repository.BugJpaRepository

@Component
class BugPersistenceAdapter(
    private val bugRepository: BugJpaRepository,
    private val bugReportMapper: BugReportMapper
) : BugPort {

    override fun saveBugReport(bugReport: BugReport) = bugReportMapper.toDomain(
        bugRepository.save(
            bugReportMapper.toEntity(bugReport)
        )
    )!!
}
