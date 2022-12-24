package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
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
        val (_, _, totalWidthSize, totalHeightSize,
            eastDescription, westDescription, southDescription, northDescription,
            _, availableGrade, seatRequests) = request

        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val isAlreadyExists = queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(
            request.floor, request.name, currentUser.schoolId
        )
        if (isAlreadyExists) {
            throw StudyRoomAlreadyExistsException
        }

        val availableHeadCount = seatRequests.count {
            SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
        }

        val studyRoom = StudyRoom(
            schoolId = currentUser.schoolId,
            name = request.name,
            floor = request.floor,
            widthSize = totalWidthSize,
            heightSize = totalHeightSize,
            inUseHeadcount = 0,
            availableHeadcount = availableHeadCount,
            availableSex = Sex.valueOf(request.availableSex),
            availableGrade = availableGrade,
            eastDescription = eastDescription,
            westDescription = westDescription,
            southDescription = southDescription,
            northDescription = northDescription
        )
        val savedStudyRoom = commandStudyRoomPort.saveStudyRoom(studyRoom)

        val seats = seatRequests.map {
            Seat(
                studyRoomId = savedStudyRoom.id,
                studentId = null,
                typeId = it.typeId,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                status = SeatStatus.valueOf(it.status)
            )
        }
        commandStudyRoomPort.saveAllSeat(seats)

        return savedStudyRoom.id
    }
}