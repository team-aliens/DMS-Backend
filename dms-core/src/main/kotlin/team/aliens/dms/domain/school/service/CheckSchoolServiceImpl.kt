package team.aliens.dms.domain.school.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.school.exception.AnswerMismatchException

@Service
class CheckSchoolServiceImpl : CheckSchoolService {

    override fun checkSchoolAnswer(answer1: String, answer2: String) {
        if (answer1 != answer2) {
            throw AnswerMismatchException
        }
    }
}
