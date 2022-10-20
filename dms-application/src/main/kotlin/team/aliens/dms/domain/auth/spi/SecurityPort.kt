package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.spi.UserSecurityPort

interface SecurityPort : ManagerSecurityPort, UserSecurityPort, StudentSecurityPort {
}