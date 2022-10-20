package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort

interface AuthCodePort : CommandAuthCodePort, QueryAuthCodePort, StudentQueryAuthCodePort {
}