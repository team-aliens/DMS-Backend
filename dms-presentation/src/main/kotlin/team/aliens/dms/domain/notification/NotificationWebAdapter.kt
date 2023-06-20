package team.aliens.dms.domain.notification

import javax.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.dto.TopicSubscriptionGroupsResponse
import team.aliens.dms.domain.notification.dto.request.DeviceTokenWebRequest
import team.aliens.dms.domain.notification.dto.request.TopicRequest
import team.aliens.dms.domain.notification.dto.request.UpdateTopicSubscriptionsWebRequest
import team.aliens.dms.domain.notification.usecase.QueryTopicSubscriptionUseCase
import team.aliens.dms.domain.notification.usecase.SetDeviceTokenUseCase
import team.aliens.dms.domain.notification.usecase.SubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UnsubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UpdateTopicSubscriptionsUseCase

@Validated
@RequestMapping("/notifications")
@RestController
class NotificationWebAdapter(
    private val setDeviceTokenUseCase: SetDeviceTokenUseCase,
    private val subscribeTopicUseCase: SubscribeTopicUseCase,
    private val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    private val updateTopicSubscriptionsUseCase: UpdateTopicSubscriptionsUseCase,
    private val queryTopicSubscriptionUseCase: QueryTopicSubscriptionUseCase
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
    fun updateTopicSubscriptions(@RequestBody @Valid request: UpdateTopicSubscriptionsWebRequest) {
        updateTopicSubscriptionsUseCase.execute(
            token = request.deviceToken,
            topicsToSubscribe = request.topicsToSubscribe.map { it.toPair() }
        )
    }

    @GetMapping("/topic")
    fun queryTopicSubscriptions(@RequestBody @Valid request: DeviceTokenWebRequest): TopicSubscriptionGroupsResponse {
        return queryTopicSubscriptionUseCase.execute(request.deviceToken)
    }
}
