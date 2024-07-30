package team.aliens.dms.domain.notification

import jakarta.validation.Valid
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
import team.aliens.dms.domain.notification.dto.NotificationsResponse
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.dto.TopicSubscriptionGroupsResponse
import team.aliens.dms.domain.notification.dto.request.DeviceTokenWebRequest
import team.aliens.dms.domain.notification.dto.request.TopicRequest
import team.aliens.dms.domain.notification.dto.request.UpdateTopicSubscriptionsWebRequest
import team.aliens.dms.domain.notification.usecase.QueryAndReadMyNotificationsUseCase
import team.aliens.dms.domain.notification.usecase.QueryTopicSubscriptionUseCase
import team.aliens.dms.domain.notification.usecase.RemoveMyAllNotificationUseCase
import team.aliens.dms.domain.notification.usecase.RemoveNotificationUseCase
import team.aliens.dms.domain.notification.usecase.SetDeviceTokenUseCase
import team.aliens.dms.domain.notification.usecase.SubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UnsubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UpdateTopicSubscriptionsUseCase
import java.util.UUID

@Validated
@RequestMapping("/notifications")
@RestController
class NotificationWebAdapter(
    private val setDeviceTokenUseCase: SetDeviceTokenUseCase,
    private val queryAndReadMyNotificationsUseCase: QueryAndReadMyNotificationsUseCase,
    private val subscribeTopicUseCase: SubscribeTopicUseCase,
    private val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    private val updateTopicSubscriptionsUseCase: UpdateTopicSubscriptionsUseCase,
    private val queryTopicSubscriptionUseCase: QueryTopicSubscriptionUseCase,
    private val removeNotificationUseCase: RemoveNotificationUseCase,
    private val removeMyAllNotificationUseCase: RemoveMyAllNotificationUseCase,
) {

    @PostMapping("/token")
    fun setDeviceToken(@RequestBody @Valid request: DeviceTokenWebRequest) {
        setDeviceTokenUseCase.execute(
            SetDeviceTokenRequest(token = request.deviceToken)
        )
    }

    @GetMapping
    fun queryMyNotifications(): NotificationsResponse {
        return queryAndReadMyNotificationsUseCase.execute()
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
    fun queryTopicSubscriptions(@RequestParam("device_token") deviceToken: String): TopicSubscriptionGroupsResponse {
        return queryTopicSubscriptionUseCase.execute(deviceToken)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{notification-of-user-id}")
    fun removeNotificationOfUser(@PathVariable("notification-of-user-id") notificationOfUserId: UUID) {
        removeNotificationUseCase.execute(notificationOfUserId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun removeMyAllNotificationOfUser() {
        removeMyAllNotificationUseCase.execute()
    }
}
