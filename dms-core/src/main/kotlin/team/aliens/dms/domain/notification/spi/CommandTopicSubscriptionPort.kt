package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.TopicSubscription

interface CommandTopicSubscriptionPort {

    fun saveTopicSubscription(topicSubscription: TopicSubscription): TopicSubscription

    fun saveAllTopicSubscriptions(topicSubscriptions: List<TopicSubscription>)
}
