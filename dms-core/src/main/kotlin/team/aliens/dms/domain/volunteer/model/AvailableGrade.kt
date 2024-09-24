package team.aliens.dms.domain.volunteer.model

enum class AvailableGrade(
    val grades: Set<Int>
) {
    ALL(setOf(1, 2, 3)),
    FIRST(setOf(1)),
    SECOND(setOf(2)),
    THIRD(setOf(3)),
    FIRST_AND_SECOND(setOf(1, 2)),
    SECOND_AND_THIRD(setOf(2, 3)),
    FIRST_AND_THIRD(setOf(1, 3))
}
