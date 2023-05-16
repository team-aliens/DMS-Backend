package team.aliens.dms.domain.user.service

import team.aliens.dms.common.annotation.Service

@Service
class UserService(
    checkUserService: CheckUserService,
    getUserService: GetUserService,
    commandUserService: CommandUserService
) : CheckUserService by checkUserService,
    GetUserService by getUserService,
    CommandUserService by commandUserService
