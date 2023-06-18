package team.aliens.dms.domain.notification.spi

import java.util.UUID
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe

interface TopicSubscribePort {

    fun saveTopicSubscribe(topicSubscribe: TopicSubscribe): TopicSubscribe

    fun queryTopicSubscribesByDeviceTokenId(deviceTokenId: UUID): List<TopicSubscribe>

    fun saveAllTopicSubscribes(topicSubscribes: List<TopicSubscribe>)

    fun deleteByDeviceTokenIdAndTopics(deviceTokenId: UUID, topics: List<Topic>)
}
