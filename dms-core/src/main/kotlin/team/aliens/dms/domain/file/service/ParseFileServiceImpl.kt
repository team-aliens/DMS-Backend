package team.aliens.dms.domain.file.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.file.spi.ParseFilePort
import java.io.File

@Service
class ParseFileServiceImpl(
    private val parseFilePort: ParseFilePort
) : ParseFileService {

    override fun getExcelStudentVO(file: File) =
        parseFilePort.getExcelStudentVO(file)
}
