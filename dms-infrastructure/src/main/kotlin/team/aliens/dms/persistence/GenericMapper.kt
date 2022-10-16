package team.aliens.dms.persistence

interface GenericMapper<D, E> {
    fun toDomain(e: E): D
    fun toEntity(d: D): E
}