package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service

@Service
class SchoolService(
    getSchoolService: GetSchoolService
) : GetSchoolService by getSchoolService
