package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicGroup
import team.aliens.dms.domain.notification.model.TopicSubscribe

data class TopicSubscribeGroupsResponse(
    val topicGroups: List<TopicGroupSubscribeResponse>
) {
    companion object {
        fun of(topicSubscribes: List<TopicSubscribe>) = TopicSubscribeGroupsResponse(
            TopicGroup.values()
                .associateWith { mutableListOf<TopicSubscribeResponse>() }
                .also { topicGroups ->
                    val topicSubscribesSet = topicSubscribes
                        .map { it.topic }
                        .toSet()
                    Topic.values().forEach {
                        topicGroups[it.topicGroup]?.add(
                            TopicSubscribeResponse(
                                topic = it,
                                isSubscribed = topicSubscribesSet.contains(it)
                            )
                        )
                    }
                }
                .map { topicGroup ->
                    TopicGroupSubscribeResponse(
                        topicGroup = topicGroup.key,
                        topicSubscribes = topicGroup.value
                    )
                }
        )
    }
}

data class TopicGroupSubscribeResponse(
    val topicGroup: TopicGroup,
    val topicSubscribes: List<TopicSubscribeResponse>
)

data class TopicSubscribeResponse(
    val topic: Topic,
    val title: String = topic.title,
    val description: String = topic.content,
    val isSubscribed: Boolean
)
