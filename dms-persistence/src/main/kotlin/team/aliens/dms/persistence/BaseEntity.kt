package team.aliens.dms.persistence

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.GenericGenerator
import java.util.UUID

@MappedSuperclass
abstract class BaseEntity(

    @Id
    @GeneratedValue(generator = "timeBasedUUID")
    @GenericGenerator(name = "timeBasedUUID", strategy = "team.aliens.dms.persistence.TimeBasedUUIDGenerator")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val id: UUID?

) : BaseTimeEntity()
