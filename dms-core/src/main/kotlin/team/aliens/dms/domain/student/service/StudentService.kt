package team.aliens.dms.domain.student.service

import team.aliens.dms.common.annotation.Service

@Service
class StudentService(
    checkStudentService: CheckStudentService,
    getStudentService: GetStudentService,
    commandStudentService: CommandStudentService
) : CheckStudentService by checkStudentService,
    GetStudentService by getStudentService,
    CommandStudentService by commandStudentService
