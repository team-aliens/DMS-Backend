package team.aliens.dms.common.util

class GCNToStringUtil {

    fun gcnToString(grade: Int, classRoom: Int, number: Int): String {
        return grade.toString().plus(classRoom).plus(number)
    }
}