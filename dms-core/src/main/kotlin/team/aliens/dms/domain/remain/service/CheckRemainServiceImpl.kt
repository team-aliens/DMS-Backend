package team.aliens.dms.domain.remain.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.remain.model.RemainAvailableTime

@Service
class CheckRemainServiceImpl : CheckRemainService {

    override fun checkRemainAvailable(remainAvailableTime: RemainAvailableTime) {
        remainAvailableTime.checkAvailable()
    }
}
