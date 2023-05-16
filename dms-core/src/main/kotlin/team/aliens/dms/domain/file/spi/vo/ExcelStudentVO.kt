package team.aliens.dms.domain.file.spi.vo

import team.aliens.dms.domain.student.model.Sex

data class ExcelStudentVO(

    val roomNumber: String,

    val roomLocation: String,

    val grade: Int,

    val classRoom: Int,

    val number: Int,

    val name: String,

    val sex: Sex
) {
    val tripleGcn
        get() = Triple(grade, classRoom, number)

    val pairRoomNumberAndLocation
        get() = Pair(roomNumber, roomLocation)
}
