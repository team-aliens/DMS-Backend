package team.aliens.dms.domain.student.stub

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student

internal fun createStudentStub(
    id: UUID = UUID.randomUUID(),
    userId: UUID = UUID.randomUUID(),
    roomId: UUID = UUID.randomUUID(),
    roomNumber: String = "415",
    roomLocation: String = "C",
    schoolId: UUID = UUID.randomUUID(),
    grade: Int = 2,
    classRoom: Int = 2,
    number: Int = 1,
    name: String = "이름",
    profileImageUrl: String = "image",
    sex: Sex = Sex.MALE,
    deletedAt: LocalDateTime? = null
) = Student(
    id = id,
    userId = userId,
    roomId = roomId,
    roomNumber = roomNumber,
    roomLocation = roomLocation,
    schoolId = schoolId,
    grade = grade,
    classRoom = classRoom,
    number = number,
    name = name,
    profileImageUrl = profileImageUrl,
    sex = sex,
    deletedAt = deletedAt
)

internal fun createVerifiedStudentStub(
    id: UUID = UUID.randomUUID(),
    schoolName: String = "대덕소마고",
    name: String = "이름",
    roomNumber: String = "415",
    roomLocation: String = "C",
    gcn: String = "2201",
    sex: Sex = Sex.MALE
) = VerifiedStudent(
    id = id,
    schoolName = schoolName,
    name = name,
    roomNumber = roomNumber,
    roomLocation = roomLocation,
    gcn = gcn,
    sex = sex
)
