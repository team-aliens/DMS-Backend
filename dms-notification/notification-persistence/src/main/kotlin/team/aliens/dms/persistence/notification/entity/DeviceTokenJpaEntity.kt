package team.aliens.dms.persistence.notification.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseUUIDEntity
import java.util.UUID

@Entity
@Table(name = "tbl_device_token")
class DeviceTokenJpaEntity(

    id: UUID?,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val userId: UUID?,

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val schoolId: UUID?,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val token: String

) : BaseUUIDEntity(id)
