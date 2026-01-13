package team.aliens.dms.persistence.notice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notice.entity.NoticeJpaEntity
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.List

@Repository
interface NoticeJpaRepository : JpaRepository<NoticeJpaEntity, UUID> {

    fun existsByCreatedAtBetween(to: LocalDateTime, from: LocalDateTime): Boolean

    fun findAllByManagerUserSchoolIdOrderByCreatedAtAsc(schoolId: UUID): List<NoticeJpaEntity>

    fun findAllByManagerUserSchoolIdOrderByCreatedAtDesc(schoolId: UUID): List<NoticeJpaEntity>

    fun findByIdAndManagerUserId(id: UUID, managerId: UUID): NoticeJpaEntity?
}
