package team.aliens.dms.common.util

object GCNToStringUtil {

    fun gcnToString(grade: Int, classRoom: Int, number: Int): String {
        return grade.toString().plus(classRoom).plus(number)
    }
}