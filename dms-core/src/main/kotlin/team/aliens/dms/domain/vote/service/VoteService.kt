package team.aliens.dms.domain.vote.service

class VoteService(
    commendVotingTopicService: CommendVotingTopicService,
    getVotingTopicService: GetVotingTopicService,
) : CommendVotingTopicService by commendVotingTopicService,
    GetVotingTopicService by getVotingTopicService
