package team.aliens.dms.domain.chatbot.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import team.aliens.dms.domain.chatbot.stub.createChatbotDocumentStub

class ChatbotDocumentTest : DescribeSpec({

    describe("isUpToDate") {
        val contentHash = "a".repeat(64)
        val embeddingModel = "gemini-embedding-001/768"

        context("원문 해시와 임베딩 모델이 모두 같으면") {
            it("true를 반환한다") {
                val document = createChatbotDocumentStub(contentHash = contentHash, embeddingModel = embeddingModel)

                document.isUpToDate(contentHash, embeddingModel) shouldBe true
            }
        }

        context("원문 해시가 다르면") {
            it("false를 반환한다") {
                val document = createChatbotDocumentStub(contentHash = contentHash, embeddingModel = embeddingModel)

                document.isUpToDate("b".repeat(64), embeddingModel) shouldBe false
            }
        }

        context("임베딩 모델이 다르면") {
            it("false를 반환한다") {
                val document = createChatbotDocumentStub(contentHash = contentHash, embeddingModel = embeddingModel)

                document.isUpToDate(contentHash, "gemini-embedding-001/3072") shouldBe false
            }
        }
    }

    describe("isAllowedExtension") {
        context("확장자가 md 또는 txt면 대소문자와 관계없이") {
            it("true를 반환한다") {
                forAll(
                    row("rules.md"),
                    row("notice.txt"),
                    row("RULES.MD"),
                    row("Notice.Txt"),
                    row("2026.dormitory.rules.md")
                ) { fileName ->
                    ChatbotDocument.isAllowedExtension(fileName) shouldBe true
                }
            }
        }

        context("확장자가 md, txt가 아니거나 없으면") {
            it("false를 반환한다") {
                forAll(
                    row("rules.pdf"),
                    row("rules.hwp"),
                    row("rules.md.exe"),
                    row("rules"),
                    row("rules."),
                    row("")
                ) { fileName ->
                    ChatbotDocument.isAllowedExtension(fileName) shouldBe false
                }
            }
        }
    }

    describe("isAllowedFileNameLength") {
        context("파일 이름이 500자 이하면") {
            it("true를 반환한다") {
                ChatbotDocument.isAllowedFileNameLength("a".repeat(497) + ".md") shouldBe true
            }
        }

        context("파일 이름이 500자를 넘으면") {
            it("false를 반환한다") {
                ChatbotDocument.isAllowedFileNameLength("a".repeat(498) + ".md") shouldBe false
            }
        }
    }

    describe("titleOf") {
        context("본문에 H1이 있으면") {
            it("첫 H1을 앞뒤 공백을 떼고 제목으로 쓴다") {
                val content = "개정 2026.03.01\n\n#   기숙사 생활 규정  \n\n## 외박\n\n# 부칙"

                ChatbotDocument.titleOf("rules.md", content) shouldBe "기숙사 생활 규정"
            }
        }

        context("H1이 없으면") {
            it("확장자를 뗀 파일 이름을 제목으로 쓴다") {
                forAll(
                    row("rules.md", "## 외박\n외박은 전날 18시까지 신청한다."),
                    row("2026.dormitory.rules.md", "본문만 있다"),
                    row("rules.txt", "#띄어쓰기 없는 샵은 H1이 아니다"),
                    row("rules", "")
                ) { fileName, content ->
                    ChatbotDocument.titleOf(fileName, content) shouldBe fileName.substringBeforeLast('.')
                }
            }
        }

        context("H1이 비어 있으면") {
            it("확장자를 뗀 파일 이름을 제목으로 쓴다") {
                ChatbotDocument.titleOf("rules.md", "#    \n\n본문") shouldBe "rules"
            }
        }

        context("제목이 100자를 넘으면") {
            it("100자로 자른다") {
                val longTitle = "가".repeat(150)

                ChatbotDocument.titleOf("rules.md", "# $longTitle") shouldBe "가".repeat(100)
                ChatbotDocument.titleOf("$longTitle.md", "본문") shouldBe "가".repeat(100)
            }
        }
    }

    describe("contentHashOf") {
        context("원문을 넣으면") {
            it("UTF-8 바이트의 SHA-256을 소문자 hex 64자로 반환한다") {
                forAll(
                    row("abc", "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"),
                    row("외박은 전날 18시까지 신청한다.", "f261e65e120f51015cf8e395f4e5c386d7184b2de127510acbf505fc5cb499fc")
                ) { content, expected ->
                    ChatbotDocument.contentHashOf(content) shouldBe expected
                }
            }
        }

        context("원문이 한 글자라도 다르면") {
            it("다른 해시를 반환한다") {
                ChatbotDocument.contentHashOf("외박은 전날 18시까지 신청한다.") shouldNotBe
                    ChatbotDocument.contentHashOf("외박은 전날 19시까지 신청한다.")
            }
        }
    }
})
