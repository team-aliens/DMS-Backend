package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service

@Service
class SchoolService(
    checkSchoolService: CheckSchoolService,
    getSchoolService: GetSchoolService,
    commandSchoolService: CommandSchoolService
) : CheckSchoolService by checkSchoolService,
    GetSchoolService by getSchoolService,
    CommandSchoolService by commandSchoolService
