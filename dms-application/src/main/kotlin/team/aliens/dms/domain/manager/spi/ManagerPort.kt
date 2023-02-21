package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.point.spi.PointQueryManagerPort

interface ManagerPort :
    QueryManagerPort,
    PointQueryManagerPort {
}