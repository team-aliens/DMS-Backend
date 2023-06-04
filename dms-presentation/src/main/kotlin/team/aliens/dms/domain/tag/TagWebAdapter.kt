package team.aliens.dms.domain.tag

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.tag.dto.*
import team.aliens.dms.domain.tag.usecase.CancelGrantedTagUseCase
import team.aliens.dms.domain.tag.usecase.CreateTagUseCase
import team.aliens.dms.domain.tag.usecase.GrantTagUseCase
import team.aliens.dms.domain.tag.usecase.QueryTagsUseCase
import team.aliens.dms.domain.tag.usecase.RemoveTagUseCase
import team.aliens.dms.domain.tag.usecase.UpdateTagUseCase
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/tags")
@RestController
class TagWebAdapter(
    private val queryTagsUseCase: QueryTagsUseCase,
    private val cancelGrantedTagUseCase: CancelGrantedTagUseCase,
    private val grantTagUseCase: GrantTagUseCase,
    private val removeTagUseCase: RemoveTagUseCase,
    private val createTagUseCase: CreateTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase
) {

    @GetMapping
    fun queryTags(): TagsResponse {
        return queryTagsUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{tag-id}")
    fun removeTag(@PathVariable("tag-id") @NotNull tagId: UUID) {
        removeTagUseCase.execute(tagId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/students")
    fun cancelGrantedTag(
        @RequestParam(name = "student_id") @NotNull studentId: UUID,
        @RequestParam(name = "tag_id") @NotNull tagId: UUID
    ) {
        cancelGrantedTagUseCase.execute(
            studentId = studentId,
            tagId = tagId
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/students")
    fun grantTag(@RequestBody @Valid request: GrantTagWebRequest) {
        grantTagUseCase.execute(
            GrantTagRequest(
                tagId = request.tagId,
                studentIds = request.studentIds
            )
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createTag(@RequestBody @Valid webRequest: CreateTagWebRequest): TagIdResponse {
        return createTagUseCase.execute(
            name = webRequest.name,
            color = webRequest.color
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{tag-id}")
    fun updateTag(
        @RequestBody @Valid webRequest: UpdateTagWebRequest,
        @PathVariable("tag-id") @NotNull tagId: UUID
    ) {
        updateTagUseCase.execute(
            tagId = tagId,
            newName = webRequest.name,
            newColor = webRequest.color
        )
    }
}
