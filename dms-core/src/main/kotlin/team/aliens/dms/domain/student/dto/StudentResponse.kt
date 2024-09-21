package team.aliens.dms.domain.student.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.vo.AllStudentsVO
import team.aliens.dms.domain.tag.dto.TagResponse
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

data class StudentResponse(
    val schoolName: String,
    val name: String,
    val gcn: String,
    val profileImageUrl: String,
    val sex: Sex,
    val bonusPoint: Int,
    val minusPoint: Int,
    val phrase: String
) {
    companion object {
        fun of(
            schoolName: String,
            student: Student,
            bonusPoint: Int,
            minusPoint: Int,
            phrase: String,
        ) = StudentResponse(
            schoolName = schoolName,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            phrase = phrase
        )
    }
}

data class StudentEmailResponse(
    val email: String
)

data class StudentNameResponse(
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StudentDetailsResponse @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("name") val name: String,
    @JsonProperty("gcn") val gcn: String,
    @JsonProperty("profileImageUrl") val profileImageUrl: String,
    @JsonProperty("sex") val sex: Sex? = null,
    @JsonProperty("bonusPoint") val bonusPoint: Int? = null,
    @JsonProperty("minusPoint") val minusPoint: Int? = null,
    @JsonProperty("roomNumber") val roomNumber: String? = null,
    @JsonProperty("roomMates") val roomMates: List<RoomMate>? = null,
    @JsonProperty("tags") val tags: List<TagResponse>? = null
) {
    data class RoomMate(
        val id: UUID,
        val name: String,
        val profileImageUrl: String
    ) {
        companion object {
            fun of(roomMates: List<Student>) = roomMates.map {
                RoomMate(
                    id = it.id,
                    name = it.name,
                    profileImageUrl = it.profileImageUrl
                )
            }
        }
    }

    companion object {
        fun of(
            student: Student,
            bonusPoint: Int,
            minusPoint: Int,
            roomMates: List<Student>,
            tags: List<Tag>
        ) = StudentDetailsResponse(
            id = student.id,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = RoomMate.of(roomMates = roomMates),
            tags = tags.map {
                TagResponse.of(it)
            }
        )
    }
}

data class StudentsResponse(
    val students: List<StudentDetailsResponse> = emptyList(),
) {
    companion object {
        @JvmName("ofStudentWithTag")
        fun of(students: List<StudentWithTag>) = StudentsResponse(
            students = students.map {
                StudentDetailsResponse(
                    id = it.id,
                    name = it.name,
                    gcn = it.gcn,
                    roomNumber = it.roomNumber,
                    profileImageUrl = it.profileImageUrl,
                    sex = it.sex,
                    bonusPoint = it.bonusPoint,
                    minusPoint = it.minusPoint,
                    tags = it.tags.map { tag ->
                        TagResponse.of(tag)
                    }
                )
            }
        )

        @JvmName("ofAllStudentsVO")
        fun of(students: List<AllStudentsVO>) = StudentsResponse(
            students = students.map {
                StudentDetailsResponse(
                    id = it.id,
                    name = it.name,
                    gcn = it.gcn,
                    profileImageUrl = it.profileImageUrl
                )
            }
        )
    }
}
