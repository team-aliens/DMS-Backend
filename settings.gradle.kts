rootProject.name = "DMS-Backend"

// main-server
include(":dms-main")
include(":dms-main:main-core")
include(":dms-main:main-persistence")
include(":dms-main:main-infrastructure")
include(":dms-main:main-presentation")

// contracts
include(":contracts")
include(":contracts:enum-contracts")
include(":contracts:enum-contracts:notification-enum")
include(":contracts:model-contracts")
include(":contracts:model-contracts:notification-model")
