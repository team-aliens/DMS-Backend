package team.aliens.dms.persistence.student.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class StudentTagId(

    @Column(nullable = false)
    val studentId: UUID,

    @Column(nullable = false)
    val tagId: UUID

) : Serializable
