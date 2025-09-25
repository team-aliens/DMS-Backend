package team.aliens.dms.domain.auth.service

import team.aliens.dms.common.annotation.Service

@Service
class AuthService(
    checkAuthCodeService: CheckAuthCodeService,
    getAuthCodeService: GetAuthCodeService,
    commandAuthCodeService: CommandAuthCodeService
) : CheckAuthCodeService by checkAuthCodeService,
    GetAuthCodeService by getAuthCodeService,
    CommandAuthCodeService by commandAuthCodeService
