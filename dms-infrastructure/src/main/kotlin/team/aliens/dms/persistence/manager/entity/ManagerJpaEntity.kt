package team.aliens.dms.persistence.manager.entity

import team.aliens.dms.persistence.user.entity.UserJpaEntity
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_manager")
class ManagerJpaEntity(

    @Id
    val managerId: UUID,

    @MapsId("managerId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: UserJpaEntity?
)