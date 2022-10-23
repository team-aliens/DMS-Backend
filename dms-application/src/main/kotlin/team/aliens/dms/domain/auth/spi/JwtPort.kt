package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.user.spi.UserJwtPort
import java.util.*

interface JwtPort : UserJwtPort, StudentJwtPort, AuthJwtPort{

}