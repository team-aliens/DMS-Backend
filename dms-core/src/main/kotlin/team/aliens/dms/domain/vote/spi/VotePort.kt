package team.aliens.dms.domain.vote.spi

interface VotePort :
    QueryVotePort,
    CommandVotePort
