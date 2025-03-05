package team.aliens.dms.domain.vote.service

import java.util.UUID

interface CheckVoteService {

    fun checkVotingTopicExistByName(name: String): Boolean

    fun checkVotingTopicExistById(id: UUID): Boolean

    fun checkVotingOptionExistById(id: UUID): Boolean

    fun checkVoteExistByStudentIdAndVotingTopicId(studentId: UUID, votingTopicId: UUID): Boolean
}
