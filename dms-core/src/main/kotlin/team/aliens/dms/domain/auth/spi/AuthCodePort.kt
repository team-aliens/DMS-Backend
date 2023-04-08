package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort

interface AuthCodePort :
    QueryAuthCodePort,
    CommandAuthCodePort,
    StudentQueryAuthCodePort,
    ManagerQueryAuthCodePort
