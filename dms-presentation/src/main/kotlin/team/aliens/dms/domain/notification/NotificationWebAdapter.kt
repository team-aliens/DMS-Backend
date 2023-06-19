package team.aliens.dms.domain.notification

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.dto.TopicSubscribeGroupsResponse
import team.aliens.dms.domain.notification.dto.request.DeviceTokenWebRequest
import team.aliens.dms.domain.notification.dto.request.TopicRequest
import team.aliens.dms.domain.notification.dto.request.UpdateTopicSubscribesWebRequest
import team.aliens.dms.domain.notification.usecase.QueryTopicSubscribesUseCase
import team.aliens.dms.domain.notification.usecase.SetDeviceTokenUseCase
import team.aliens.dms.domain.notification.usecase.SubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UnsubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UpdateTopicSubscribesUseCase
import javax.validation.Valid

@Validated
@RequestMapping("/notifications")
@RestController
class NotificationWebAdapter(
    private val setDeviceTokenUseCase: SetDeviceTokenUseCase,
    private val subscribeTopicUseCase: SubscribeTopicUseCase,
    private val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    private val updateTopicSubscribesUseCase: UpdateTopicSubscribesUseCase,
    private val queryTopicSubscribesUseCase: QueryTopicSubscribesUseCase
) {

    @PostMapping("/token")
    fun setDeviceToken(@RequestBody @Valid request: DeviceTokenWebRequest) {
        setDeviceTokenUseCase.execute(
            SetDeviceTokenRequest(token = request.deviceToken)
        )
    }

    @PostMapping("/topic")
    fun subscribeTopic(@RequestBody @Valid request: TopicRequest) {
        subscribeTopicUseCase.execute(
            token = request.token,
            topic = request.topic
        )
    }

    @DeleteMapping("/topic")
    fun unsubscribeTopic(@RequestBody @Valid request: TopicRequest) {
        unsubscribeTopicUseCase.execute(
            token = request.token,
            topic = request.topic
        )
    }

    @PatchMapping("/topic")
    fun updateTopicSubscribes(@RequestBody @Valid request: UpdateTopicSubscribesWebRequest) {
        updateTopicSubscribesUseCase.execute(
            token = request.deviceToken,
            topicsToSubscribe = request.topicsToSubscribe.map { it.toPair() }
        )
    }

    @GetMapping("/topic")
    fun queryTopicSubscribes(@RequestBody @Valid request: DeviceTokenWebRequest): TopicSubscribeGroupsResponse {
        return queryTopicSubscribesUseCase.execute(request.deviceToken)
    }
}
