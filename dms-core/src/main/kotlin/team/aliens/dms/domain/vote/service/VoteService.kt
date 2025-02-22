package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service

@Service
class VoteService(
    commandVoteService: CommandVoteService,
    getVoteService: GetVoteService,
) : CommandVoteService by commandVoteService,
    GetVoteService by getVoteService
