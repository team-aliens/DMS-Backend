package team.aliens.dms.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

/**
 *
 * 학생 정보를 관리하는 서비스 StudentDetailsService
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
@Component
class StudentDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}