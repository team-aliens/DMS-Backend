package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class CreateStudyRoomUseCase(
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort
) {

    fun execute(request: CreateStudyRoomRequest): UUID {

        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val isAlreadyExists = queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(
            floor = request.floor,
            name = request.name,
            schoolId = currentUser.schoolId
        )
        if (isAlreadyExists) {
            throw StudyRoomAlreadyExistsException
        }

        val studyRoom = request.run {
            StudyRoom(
                schoolId = currentUser.schoolId,
                name = name,
                floor = floor,
                widthSize = totalWidthSize,
                heightSize = totalHeightSize,
                availableHeadcount = request.seats.count {
                    SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
                },
                availableSex = Sex.valueOf(availableSex),
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription
            )
        }
        val savedStudyRoom = commandStudyRoomPort.saveStudyRoom(studyRoom)

        val studyRoomTimeSlots = request.timeSlotIds.map {
            StudyRoomTimeSlot(
                studyRoomId = savedStudyRoom.id,
                timeSlotId = it
            )
        }
        commandStudyRoomPort.saveAllStudyRoomTimeSlots(studyRoomTimeSlots)

        val seats = request.seats.map {
            Seat(
                studyRoomId = savedStudyRoom.id,
                typeId = it.typeId,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                status = SeatStatus.valueOf(it.status)
            )
        }
        commandStudyRoomPort.saveAllSeats(seats)

        return savedStudyRoom.id
    }
}
