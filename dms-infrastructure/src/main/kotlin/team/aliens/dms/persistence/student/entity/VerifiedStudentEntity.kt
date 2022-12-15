package team.aliens.dms.persistence.student.entity

import com.fasterxml.uuid.Generators
import java.util.UUID
import javax.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("tbl_verified_student")
class VerifiedStudentEntity(

    @Id
    val id: UUID = Generators.timeBasedGenerator().generate(),

    @field:NotNull
    val schoolName: String,

    @field:NotNull
    val name: String,

    @field:NotNull
    val roomNumber: Int,

    @field:NotNull
    val gcn: String

)