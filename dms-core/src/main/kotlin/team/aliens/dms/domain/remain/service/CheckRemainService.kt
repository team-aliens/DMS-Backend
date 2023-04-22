package team.aliens.dms.domain.remain.service

import team.aliens.dms.domain.remain.model.RemainAvailableTime

interface CheckRemainService {

    fun checkRemainAvailable(remainAvailableTime: RemainAvailableTime)
}
