package team.aliens.dms.persistence.notification.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import java.util.UUID

@Repository
interface DeviceTokenJpaRepository : CrudRepository<DeviceTokenJpaEntity, UUID> {
    fun findByToken(token: String): DeviceTokenJpaEntity?

    // 파생 쿼리(findByUserId)는 tbl_user 를 조인하면서 @SQLRestriction(deleted_at is null)까지 붙여
    // 탈퇴(소프트 삭제)한 유저의 토큰을 못 찾는다. 탈퇴 후 토큰 삭제가 이 조회를 타므로 FK 컬럼을 직접 비교한다
    @Query("select d from DeviceTokenJpaEntity d where d.user.id = :userId")
    fun findByUserId(@Param("userId") userId: UUID): DeviceTokenJpaEntity?

    @Query("select count(d) > 0 from DeviceTokenJpaEntity d where d.user.id = :userId")
    fun existsByUserId(@Param("userId") userId: UUID): Boolean
}
