package team.aliens.dms.persistence.student.entity

import java.util.UUID
import javax.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("tbl_verified_student")
class VerifiedStudentEntity(

    @Id
    val id: UUID,

    @field:NotNull
    val schoolName: String,

    @field:NotNull
    val name: String,

    @field:NotNull
    val roomNumber: Int,

    @field:NotNull
    val grade: Int,

    @field:NotNull
    val classRoom: Int,

    @field:NotNull
    val number: Int

)