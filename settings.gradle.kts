rootProject.name = "DMS-Backend"

// dms-server
include(":dms-main")
include(":dms-main:main-core")
include(":dms-main:main-persistence")
include(":dms-main:main-infrastructure")
include(":dms-main:main-presentation")

// notification-server
include(":dms-notification")
include(":dms-notification:notification-core")
include(":dms-notification:notification-persistence")
include(":dms-notification:notification-infrastructure")
include(":dms-notification:notification-presentation")

// contracts
include(":contracts")
include(":contracts:enum-contracts")
include(":contracts:enum-contracts:notification-enum")

include(":contracts:model-contracts")
include(":contracts:model-contracts:notification-model")

include(":contracts:remote-contracts")
include(":contracts:remote-contracts:notification-remote")
include(":contracts:remote-contracts:notification-remote:grpc-proto")
include(":contracts:remote-contracts:notification-remote:rabbitmq-message")
