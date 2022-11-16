package team.aliens.dms.domain.manager.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Manager(

    val managerId: UUID,

    val schoolId: UUID,

    val name: String,

    val profileImageUrl: String? = PROFILE_IMAGE

) {

    companion object {
        const val PROFILE_IMAGE = "a" // TODO 기본 프로필 이미지 넣기
    }
}