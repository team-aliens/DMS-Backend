package team.aliens.dms.persistence

interface GenericMapper<D, E> {
    fun toDomain(entity: E?): D?
    fun toEntity(domain: D): E
}