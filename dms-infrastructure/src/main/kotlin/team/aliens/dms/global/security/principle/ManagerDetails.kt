package team.aliens.dms.global.security.principle

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 *
 * 관리자 정보를 담는 객체 ManagerDetails
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
class ManagerDetails : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(TODO("ROLE")))
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String = TODO("MANAGER_ID")

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}