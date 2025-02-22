package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service

@Service
class VoteService(
    commandVotingTopicService: CommandVotingTopicService,
    getVotingTopicService: GetVotingTopicService,
) : CommandVotingTopicService by commandVotingTopicService,
    GetVotingTopicService by getVotingTopicService
