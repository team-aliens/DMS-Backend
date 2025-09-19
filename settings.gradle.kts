rootProject.name = "DMS-Backend"

include("dms-main")
include("dms-main:main-core")
include("dms-main:main-persistence")
include("dms-main:main-infrastructure")
include("dms-main:main-presentation")

include("dms-notification")
include("dms-notification:notification-core")
include("dms-notification:notification-persistence")
include("dms-notification:notification-infrastructure")
include("dms-notification:notification-presentation")

include("contracts")
include("contracts:notification-contract")
include("contracts:notification-proto")

include("dms-gateway:gateway-core")
include("dms-gateway:gateway-infrastructure")
