package team.aliens.dms.global.security.principle

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class ManagerDetails : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority("ROLE"))

    override fun getPassword() = null

    override fun getUsername() = "ManagerId"

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}