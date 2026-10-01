package team.aliens.dms.persistence.notification.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import java.util.UUID

interface NotificationOfUserJpaRepository : CrudRepository<NotificationOfUserJpaEntity, UUID> {
    // 파생 쿼리는 tbl_user 의 @SQLRestriction 이 붙어 탈퇴한 유저의 알림을 못 찾으므로 FK 컬럼을 직접 비교한다
    @Query("select n from NotificationOfUserJpaEntity n where n.user.id = :userId")
    fun findByUserId(@Param("userId") userId: UUID): List<NotificationOfUserJpaEntity>
}
