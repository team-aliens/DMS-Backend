package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort

interface StudentPort : QueryStudentPort, CommandStudentPort, AuthQueryStudentPort {
}