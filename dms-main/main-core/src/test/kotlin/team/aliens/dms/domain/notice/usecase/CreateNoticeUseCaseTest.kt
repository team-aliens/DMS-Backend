package team.aliens.dms.domain.notice.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.service.NoticeService
import java.util.UUID

class CreateNoticeUseCaseTest : DescribeSpec({

    val noticeService = mockk<NoticeService>()
    val securityService = mockk<SecurityService>()

    val useCase = CreateNoticeUseCase(noticeService, securityService)

    describe("execute"){
        context("사감선생님이 공지를 생성하면"){

            val managerId = UUID.randomUUID()
            val noticeId = UUID.randomUUID()

            it("공지를 생성한다"){
                // given
                every { securityService.getCurrentUserId() } returns managerId
                every { noticeService.saveNotice(any()) } answers {
                    val arg = firstArg<Notice>()
                    Notice(
                        id = noticeId,
                        managerId = arg.managerId,
                        title = arg.title,
                        content = arg.content,
                        createdAt = arg.createdAt,
                        updatedAt = arg.updatedAt
                    )
                }

                // when
                useCase.execute("제목", "내용")

                // then
                verify(exactly = 1) { securityService.getCurrentUserId() }
                verify(exactly = 1) { noticeService.saveNotice(match {
                    it.managerId == managerId &&
                    it.title == "제목" &&
                    it.content == "내용"
                })
                }
            }
        }
    }
})
