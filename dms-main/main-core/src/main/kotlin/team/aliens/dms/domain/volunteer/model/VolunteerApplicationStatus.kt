package team.aliens.dms.domain.volunteer.model

enum class VolunteerApplicationStatus {
    NOT_APPLIED,
    APPLYING,
    APPLIED;

    companion object {

        fun of(isApproved: Boolean?): VolunteerApplicationStatus =
            when (isApproved) {
                true -> APPLIED
                false -> APPLYING
                null -> NOT_APPLIED
            }
    }
}
