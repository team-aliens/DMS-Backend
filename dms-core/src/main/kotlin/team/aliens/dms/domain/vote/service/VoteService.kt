package team.aliens.dms.domain.vote.service

class VoteService(
    val commendVotingTopicService: CommendVotingTopicService,
    val getVotingTopicService: GetVotingTopicService,
) : CommendVotingTopicService by commendVotingTopicService,
    GetVotingTopicService by getVotingTopicService
