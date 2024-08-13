package team.aliens.dms.thirdparty.slack

import com.slack.api.Slack
import com.slack.api.model.block.Blocks.asBlocks
import com.slack.api.model.block.Blocks.divider
import com.slack.api.model.block.Blocks.header
import com.slack.api.model.block.Blocks.image
import com.slack.api.model.block.Blocks.section
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.composition.OptionObject
import com.slack.api.model.block.element.BlockElements.staticSelect
import com.slack.api.webhook.Payload
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import team.aliens.dms.domain.bug.model.BugAttachment
import team.aliens.dms.domain.bug.model.BugReport
import team.aliens.dms.domain.bug.spi.SendBugPort
import java.lang.Exception

@Component
class SlackAdapter(
    @Value("\${slack.url}")
    private val url: String,

) : SendBugPort {

    private val slack = Slack.getInstance()

    override fun sendBugReport(bugReport: BugReport) {
        val blocks = toMutableBlocks(
            header { it.text(plainText("버그 제보가 들어왔습니다.")) },
            section { it.text(plainText("제보자 : ${bugReport.studentId}")) },
            section { it.text(plainText("OS : ${bugReport.developmentArea.name}")) },
            section { it.text(plainText("message : ${bugReport.content}")) }
        )

        blocks
            .addImages(bugReport.attachmentUrls?.attachmentUrls ?: emptyList())
            .addSelectProgress()
            .sendBug()
    }

    fun sendServerBug(request: HttpServletRequest, response: HttpServletResponse, exception: Exception) {
        val blocks = toMutableBlocks(
            header { it.text(plainText("서버에 예외가 발생했습니다.")) },
            section { it.text(plainText("requested info : <${request.method}> ${request.requestURL}")) },
            section { it.text(plainText("exception class : ${exception.javaClass.name}")) },
            section { it.text(plainText("exception message : ${exception.message}")) },
        )

        blocks
            .addSelectProgress()
            .sendBug()
    }

    private fun toMutableBlocks(vararg blocks: LayoutBlock): MutableList<LayoutBlock> {
        return blocks.toMutableList()
    }

    private fun MutableList<LayoutBlock>.addSelectProgress(): MutableList<LayoutBlock> {
        this.addAll(
            listOf(
                divider(),
                section { section ->
                    section.text(markdownText("현재 버그 상황을 선택 해주세요."))
                    section.accessory(
                        staticSelect {
                            it.placeholder(plainText("Select an progress status"))
                            it.options(
                                listOf(
                                    OptionObject.builder().text(plainText("미해결")).value("not-in-progress").build(),
                                    OptionObject.builder().text(plainText("해결중")).value("in-progress").build(),
                                    OptionObject.builder().text(plainText("해결됨")).value("solved").build(),
                                    OptionObject.builder().text(plainText("논외")).value("out-of-topic").build(),
                                )
                            )
                            it.initialOption(
                                OptionObject.builder().text(plainText("미해결")).value("not-in-progress").build()
                            )
                        }
                    )
                },
                divider()
            )
        )

        return this
    }

    private fun MutableList<LayoutBlock>.addImages(attachmentUrls: List<String>): MutableList<LayoutBlock> {
        this.addAll(
            attachmentUrls.map { url ->
                asBlocks(
                    image {
                        it.imageUrl(url).altText("bug image")
                    }
                )[0]
            }
        )

        return this
    }

    private fun MutableList<LayoutBlock>.sendBug() {
        slack.send(
            url,
            Payload.builder()
                .blocks(this)
                .build()
        )
    }
}
