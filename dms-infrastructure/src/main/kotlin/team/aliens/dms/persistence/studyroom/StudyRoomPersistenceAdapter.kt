package team.aliens.dms.persistence.studyroom

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val studyRoomRepository: StudyRoomJpaRepository
) : StudyRoomPort {

    override fun queryStudyRoomById(studyRoomId: UUID) = studyRoomMapper.toDomain(
        studyRoomRepository.findByIdOrNull(studyRoomId)
    )
}