package team.aliens.dms.persistence.vote.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.vote.entity.VoteJpaEntity
import team.aliens.dms.persistence.vote.entity.VotingOptionJpaEntity
import team.aliens.dms.persistence.vote.repository.VotingOptionJpaRepository
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository

@Component
class VoteMapper(
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val studentRepository: StudentJpaRepository,
    private val votingOptionJpaRepository: VotingOptionJpaRepository,
) : GenericMapper<Vote, VoteJpaEntity> {

    override fun toDomain(entity: VoteJpaEntity?): Vote? {
        return entity?.let {
            Vote(
                id = it.id!!,
                votingTopicId = it.votingTopic!!.id!!,
                studentId = it.student!!.id!!,
                votedAt = it.votedAt,
                selectedOptionId = it.selectedOption?.id,
                selectedStudentId = it.selectedStudent?.id
            )
        }
    }

    override fun toEntity(domain: Vote): VoteJpaEntity {
        val votingTopic = votingTopicJpaRepository.findByIdOrNull(domain.votingTopicId)
        val student = studentRepository.findByIdOrNull(domain.studentId)
        val selectedOption: VotingOptionJpaEntity? = domain.selectedOptionId?.let {
            votingOptionJpaRepository.findByIdOrNull(it)
        }
        val selectedStudent: StudentJpaEntity? = domain.selectedStudentId?.let {
            studentRepository.findByIdOrNull(it)
        }
        return VoteJpaEntity(
            id = domain.id,
            votingTopic = votingTopic,
            student = student,
            votedAt = domain.votedAt,
            selectedOption = selectedOption,
            selectedStudent = selectedStudent
        )
    }
}
