package team.aliens.dms.domain.manager.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.util.UUID

@Aggregate
data class Manager(

    val id: UUID,

    override val schoolId: UUID,

    val name: String,

    val profileImageUrl: String? = PROFILE_IMAGE

) : SchoolIdDomain {

    companion object {
        const val PROFILE_IMAGE = "a" // TODO 기본 프로필 이미지 넣기
    }
}
