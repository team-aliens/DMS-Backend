package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort

interface AuthCodePort : CommandAuthCodePort, QueryAuthCodePort, StudentQueryAuthCodePort, ManagerQueryAuthCodePort {
}