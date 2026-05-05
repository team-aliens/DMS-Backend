package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service

@Service
class DaybreakService(
    getDaybreakService: GetDaybreakService,
    commandDaybreakService: CommandDaybreakService,
    checkDaybreakService: CheckDaybreakService
) : GetDaybreakService by getDaybreakService,
    CommandDaybreakService by commandDaybreakService,
    CheckDaybreakService by checkDaybreakService
