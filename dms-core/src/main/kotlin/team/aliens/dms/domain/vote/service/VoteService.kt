package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service

@Service
class VoteService(
    commendVotingTopicService: CommendVotingTopicService,
    getVotingTopicService: GetVotingTopicService,
) : CommendVotingTopicService by commendVotingTopicService,
    GetVotingTopicService by getVotingTopicService
