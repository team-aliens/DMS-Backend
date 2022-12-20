package team.aliens.dms.persistence.studyroom

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimePort
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomAvailableTimeMapper
import team.aliens.dms.persistence.studyroom.repository.StudyRoomAvailableTimeJpaRepository

@Component
class StudyRoomAvailableTimePersistenceAdapter(
    private val studyRoomAvailableTimeMapper: StudyRoomAvailableTimeMapper,
    private val studyRoomAvailableTimeRepository: StudyRoomAvailableTimeJpaRepository
) : StudyRoomAvailableTimePort {

    override fun queryStudyRoomAvailableTimeBySchoolId(schoolId: UUID) = studyRoomAvailableTimeMapper.toDomain(
        studyRoomAvailableTimeRepository.findByIdOrNull(schoolId)
    )
}