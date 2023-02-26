package team.aliens.dms.global.security.principle

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

class ManagerDetails(
    private val managerId: UUID
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(Authority.MANAGER.name))
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String = managerId.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
