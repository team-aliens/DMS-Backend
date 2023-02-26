package team.aliens.dms.persistence

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseEntity(

    @Id
    @GeneratedValue(generator = "timeBasedUUID")
    @GenericGenerator(name = "timeBasedUUID", strategy = "team.aliens.dms.persistence.TimeBasedUUIDGenerator")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val id: UUID?

) : BaseTimeEntity()
