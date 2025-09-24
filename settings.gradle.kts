rootProject.name = "DMS-Backend"

include("dms-main")
include("dms-main:main-core")
include("dms-main:main-persistence")
include("dms-main:main-infrastructure")
include("dms-main:main-presentation")

include("dms-gateway:gateway-core")
include("dms-gateway:gateway-infrastructure")
