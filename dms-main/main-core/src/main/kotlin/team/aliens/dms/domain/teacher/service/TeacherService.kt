package team.aliens.dms.domain.teacher.service

import team.aliens.dms.common.annotation.Service

@Service
class TeacherService(
    getTeacherService: GetTeacherService,
) : GetTeacherService by getTeacherService
