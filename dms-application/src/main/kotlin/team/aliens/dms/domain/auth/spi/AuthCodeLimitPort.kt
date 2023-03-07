package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentQueryAuthCodeLimitPort

interface AuthCodeLimitPort :
    QueryAuthCodeLimitPort,
    StudentQueryAuthCodeLimitPort,
    CommandAuthCodeLimitPort
