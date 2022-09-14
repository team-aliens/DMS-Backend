package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

/**
*
* 관리자 정보를 관리하는 서비스 ManagerDetailsService
*
* @author jeongyoon
* @date 2022-09-15
*/
@Component
class ManagerDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}