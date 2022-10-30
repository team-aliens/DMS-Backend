package team.aliens.dms.persistence

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseTimeEntity(

    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME")
    val createdAt: LocalDateTime = LocalDateTime.now()

)