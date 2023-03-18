package team.aliens.dms.domain.student.stub

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.VerifiedStudent
import java.util.UUID

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