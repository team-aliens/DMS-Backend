package team.aliens.dms.domain.user.service

import team.aliens.dms.common.annotation.Service

@Service
class UserService(
    getUserService: GetUserService,
    commandUserService: CommandUserService
) : GetUserService by getUserService,
    CommandUserService by commandUserService
