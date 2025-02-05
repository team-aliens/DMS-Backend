package team.aliens.dms.domain.vote.service

class VotingTopicService(
    commendVotingTopicService: CommendVotingTopicService,
    scheduleCreateVoteResultNoticeService: ScheduleCreateVoteResultNoticeService,
    validVotePeriodService: ValidVotePeriodService
) : CommendVotingTopicService by commendVotingTopicService,
    ScheduleCreateVoteResultNoticeService by scheduleCreateVoteResultNoticeService,
    ValidVotePeriodService by validVotePeriodService