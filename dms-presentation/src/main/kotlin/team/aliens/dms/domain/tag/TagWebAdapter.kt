package team.aliens.dms.domain.tag

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.tag.dto.CreateTagWebRequest
import team.aliens.dms.domain.tag.dto.CreateTagWebResponse
import team.aliens.dms.domain.tag.dto.QueryTagsResponse
import team.aliens.dms.domain.tag.usecase.CreateTagUseCase
import team.aliens.dms.domain.tag.usecase.QueryTagsUseCase
import javax.validation.Valid

@Validated
@RequestMapping("/tags")
@RestController
class TagWebAdapter(
    private val queryTagsUseCase: QueryTagsUseCase,
    private val createTagUseCase: CreateTagUseCase
) {

    @GetMapping
    fun queryTags(): QueryTagsResponse {
        return queryTagsUseCase.execute()
    }

    @PostMapping
    fun createTag(@RequestBody @Valid webRequest: CreateTagWebRequest): CreateTagWebResponse {
        val tagId = createTagUseCase.execute(
            name = webRequest.name,
            color = webRequest.color
        )

        return CreateTagWebResponse(tagId)
    }
}
