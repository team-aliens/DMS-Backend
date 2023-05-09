package team.aliens.dms.persistence

import team.aliens.dms.common.annotation.Decrypted
import team.aliens.dms.common.annotation.Encrypt

interface EncryptableGenericMapper<D, E> : GenericMapper<D, E> {

    override fun toDomain(@Decrypted entity: E?): D?

    @Encrypt
    override fun toEntity(domain: D): E
}
