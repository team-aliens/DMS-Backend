package team.aliens.dms.domain.file.spi

import java.util.UUID

interface FileSecurityPort {
    fun getCurrentUserId(): UUID
}
