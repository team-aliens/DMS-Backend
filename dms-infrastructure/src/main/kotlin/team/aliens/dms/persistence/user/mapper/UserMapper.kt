package team.aliens.dms.persistence.user.mapper

import org.mapstruct.Mapper
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.user.entity.UserEntity

@Mapper
interface UserMapper : GenericMapper<User, UserEntity> {

    override fun toDomain(e: UserEntity): User

    override fun toEntity(d: User): UserEntity
}