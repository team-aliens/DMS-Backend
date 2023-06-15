package team.aliens.dms.domain.notification

import javax.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.dto.request.DeviceTokenRequest
import team.aliens.dms.domain.notification.dto.request.TopicRequest
import team.aliens.dms.domain.notification.dto.request.UpdateTopicSubscribesWebRequest
import team.aliens.dms.domain.notification.usecase.SetDeviceTokenUseCase
import team.aliens.dms.domain.notification.usecase.SubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UnsubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UpdateTopicSubscribesUseCase

@Validated
@RequestMapping("/notifications")
@RestController
class NotificationWebAdapter(
    private val setDeviceTokenUseCase: SetDeviceTokenUseCase,
    private val subscribeTopicUseCase: SubscribeTopicUseCase,
    private val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    private val updateTopicSubscribesUseCase: UpdateTopicSubscribesUseCase
) {

    @PostMapping("/token")
    fun setDeviceToken(@RequestBody @Valid request: DeviceTokenRequest) {
        setDeviceTokenUseCase.execute(
            SetDeviceTokenRequest(deviceToken = request.token)
        )
    }

    @PostMapping("/topic")
    fun subscribeTopic(@RequestBody @Valid request: TopicRequest) {
        subscribeTopicUseCase.execute(
            deviceToken = request.deviceToken,
            topic = request.topic
        )
    }

    @DeleteMapping("/topic")
    fun unsubscribeTopic(@RequestBody @Valid request: TopicRequest) {
        unsubscribeTopicUseCase.execute(
            deviceToken = request.deviceToken,
            topic = request.topic
        )
    }

    @PatchMapping("/topic")
    fun updateTopicSubscribes(@RequestBody @Valid request: UpdateTopicSubscribesWebRequest) {
        updateTopicSubscribesUseCase.execute(
            deviceToken = request.deviceToken,
            topicsToSubscribe = request.topicsToSubscribe.map { it.toPair() }
        )
    }
}
