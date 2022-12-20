package team.aliens.dms.persistence.student.entity

import java.util.UUID
import javax.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.student.model.Sex
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

@RedisHash("tbl_verified_student")
class VerifiedStudentEntity(

    @Id
    val id: UUID,

    @field:Indexed
    @field:NotNull
    val schoolName: String,

    @field:NotNull
    val name: String,

    @field:NotNull
    val roomNumber: Int,

    @field:Indexed
    @field:NotNull
    val gcn: String,

    @Column(columnDefinition = "VARCHAR(6)", nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex

)