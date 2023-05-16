package team.aliens.dms.domain.user

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.exception.InvalidRoleException

fun checkUserAuthority(authority: Authority, expectedAuthority: Authority) {
    if (authority != expectedAuthority) {
        throw InvalidRoleException
    }
}
