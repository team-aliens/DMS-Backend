package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicGroup
import team.aliens.dms.domain.notification.model.TopicSubscription

data class TopicSubscriptionGroupsResponse(
    val topicGroups: List<TopicGroupSubscriptionResponse>
) {
    companion object {
        fun of(topicSubscriptions: List<TopicSubscription>): TopicSubscriptionGroupsResponse {

            return TopicSubscriptionGroupsResponse(
                TopicGroup.values()
                    .associateWith { mutableListOf<TopicSubscriptionResponse>() }
                    .also { topicGroupsMap ->
                        addTopicSubscriptionsToTopicGroupsMap(
                            topicGroupsMap = topicGroupsMap,
                            topicSubscriptionsMap = topicSubscriptions.associateBy { it.topic }
                        )
                    }
                    .map {
                        TopicGroupSubscriptionResponse(
                            topicGroup = it.key,
                            topicSubscriptions = it.value
                        )
                    }
            )
        }

        private fun addTopicSubscriptionsToTopicGroupsMap(
            topicGroupsMap: Map<TopicGroup, MutableList<TopicSubscriptionResponse>>,
            topicSubscriptionsMap: Map<Topic, TopicSubscription>
        ) {
            Topic.values().forEach {
                topicGroupsMap[it.topicGroup]?.add(
                    TopicSubscriptionResponse(
                        topic = it,
                        isSubscribed = topicSubscriptionsMap[it]?.isSubscribed ?: false
                    )
                )
            }
        }
    }
}

data class TopicGroupSubscriptionResponse(
    val topicGroup: TopicGroup,
    val groupName: String = topicGroup.groupName,
    val topicSubscriptions: List<TopicSubscriptionResponse>
)

data class TopicSubscriptionResponse(
    val topic: Topic,
    val title: String = topic.title,
    val description: String = topic.content,
    val isSubscribed: Boolean
)
