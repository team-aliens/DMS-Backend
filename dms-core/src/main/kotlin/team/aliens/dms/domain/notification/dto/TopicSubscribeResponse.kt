package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicGroup
import team.aliens.dms.domain.notification.model.TopicSubscribe

data class TopicSubscribeGroupsResponse(
    val topicGroups: List<TopicGroupSubscribeResponse>
) {
    companion object {
        fun of(topicSubscribes: List<TopicSubscribe>): TopicSubscribeGroupsResponse {

            return TopicSubscribeGroupsResponse(
                TopicGroup.values()
                    .associateWith { mutableListOf<TopicSubscribeResponse>() }
                    .also {
                        addTopicSubscribesToTopicGroupsMap(
                            topicGroupsMap = it,
                            topicSubscribesMap = topicSubscribes.associateBy { it.topic }
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
            topicSubscribesMap: Map<Topic, TopicSubscribe>
        ) {
            Topic.values().forEach {
                topicGroupsMap[it.topicGroup]?.add(
                    TopicSubscribeResponse(
                        topic = it,
                        isSubscribed = topicSubscribesMap[it]?.isSubscribed ?: false
                    )
                )
            }
        }
    }
}

data class TopicGroupSubscribeResponse(
    val topicGroup: TopicGroup,
    val groupName: String = topicGroup.groupName,
    val topicSubscribes: List<TopicSubscribeResponse>
)

data class TopicSubscribeResponse(
    val topic: Topic,
    val title: String = topic.title,
    val description: String = topic.content,
    val isSubscribed: Boolean
)
