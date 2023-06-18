package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicGroup
import team.aliens.dms.domain.notification.model.TopicSubscribe

data class TopicSubscribeGroupsResponse(
    val topicGroups: List<TopicGroupSubscribeResponse>
) {
    companion object {
        fun of(topicSubscribes: List<TopicSubscribe>): TopicSubscribeGroupsResponse {

            val topicSubscribesSet = topicSubscribes
                .map { it.topic }
                .toSet()

            return TopicSubscribeGroupsResponse(
                TopicGroup.values()
                    .associateWith { mutableListOf<TopicSubscribeResponse>() }
                    .also {
                        addTopicSubscribesToTopicGroupsMap(
                            topicGroupsMap = it,
                            topicSubscribesSet = topicSubscribesSet
                        )
                    }
                    .map {
                        TopicGroupSubscribeResponse(
                            topicGroup = it.key,
                            topicSubscribes = it.value
                        )
                    }
            )
        }

        private fun addTopicSubscribesToTopicGroupsMap(
            topicGroupsMap: Map<TopicGroup, MutableList<TopicSubscribeResponse>>,
            topicSubscribesSet: Set<Topic>
        ) {
            Topic.values().forEach {
                topicGroupsMap[it.topicGroup]?.add(
                    TopicSubscribeResponse(
                        topic = it,
                        isSubscribed = topicSubscribesSet.contains(it)
                    )
                )
            }
        }
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
