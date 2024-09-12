package team.aliens.dms.domain.volunteer.model

enum class GradeCondition(
    val grades: Set<Int>
) {
    ALL(setOf(1, 2, 3)),
    FIRST(setOf(1)),
    SECOND(setOf(2)),
    THIRD(setOf(3)),
    FIRSTANDSECOND(setOf(1, 2)),
    SECONDANDTHIRD(setOf(2, 3)),
    FIRSTANDTHIRD(setOf(1, 3))
}
