package team.aliens.dms.domain.user.spi

interface UserPort :
    QueryUserPort,
    CommandUserPort
