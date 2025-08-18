package team.aliens.dms.domain.file.service

import team.aliens.dms.common.annotation.Service

@Service
class FileService(
    writeFileService: WriteFileService,
    parseFileService: ParseFileService
) : WriteFileService by writeFileService,
    ParseFileService by parseFileService
