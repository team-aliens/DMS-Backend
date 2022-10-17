package team.aliens.dms.domain.user.model

import team.aliens.dms.global.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class User(

    val id: UUID?,

    val schoolId: UUID,

    val accountId: String,

    val password: String,

    val email: String,

    val name: String,

    val profileImageUrl: String,

    val createdAt: LocalDateTime?,

    val deletedAt: LocalDateTime?
) {

    companion object {
        const val PROFILE_IMAGE = "a" // TODO 기본 프로필 이미지 넣기
    }
}