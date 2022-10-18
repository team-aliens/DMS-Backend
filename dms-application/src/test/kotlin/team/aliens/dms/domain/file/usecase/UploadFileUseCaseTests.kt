package team.aliens.dms.domain.file.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.file.exception.FileInvalidExtensionException
import team.aliens.dms.domain.file.spi.UploadFilePort
import java.io.File

@ExtendWith(SpringExtension::class)
class UploadFileUseCaseTests {

    @MockBean
    private lateinit var uploadFilePort: UploadFilePort

    private lateinit var uploadFileUseCase: UploadFileUseCase

    private val filePathStub = "test path"

    private val jpgFileStub by lazy { File("test.jpg") }

    private val jpgFileStub2 by lazy { File("test.JPG") }

    private val jpegFileStub by lazy { File("test.jpeg") }

    private val jpegFileStub2 by lazy { File("test.JPEG") }

    private val pngFileStub by lazy { File("test.png") }

    private val pngFileStub2 by lazy { File("test.PNG") }

    private val heicFileStub by lazy { File("test.heic") }

    private val heicFileStub2 by lazy { File("test.HEIC") }

    private val svgFileStub by lazy { File("test.svg") }

    @BeforeEach
    fun setUp() {
        uploadFileUseCase = UploadFileUseCase(uploadFilePort)
    }

    @Test
    fun `jpg 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(jpgFileStub))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(jpgFileStub)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `JPG 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(jpgFileStub2))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(jpgFileStub2)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `jpeg 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(jpegFileStub))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(jpegFileStub)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `JPEG 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(jpegFileStub2))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(jpegFileStub2)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `png 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(pngFileStub))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(pngFileStub)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `PNG 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(pngFileStub2))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(pngFileStub2)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `heic 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(heicFileStub))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(heicFileStub)

        // then
        assertEquals(response, filePathStub)
    }

    @Test
    fun `HEIC 파일 업로드 성공`() {
        // given
        given(uploadFilePort.upload(heicFileStub2))
            .willReturn(filePathStub)

        // when
        val response = uploadFileUseCase.execute(heicFileStub2)

        // then
        assertEquals(response, filePathStub)
    }


    @Test
    fun `파일 확장자 잘못됨`() {
        assertThrows<FileInvalidExtensionException> {
            uploadFileUseCase.execute(svgFileStub)
        }
    }

}