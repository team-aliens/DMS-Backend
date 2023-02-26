package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.user.spi.UserJwtPort

interface JwtPort :
    UserJwtPort,
    StudentJwtPort
