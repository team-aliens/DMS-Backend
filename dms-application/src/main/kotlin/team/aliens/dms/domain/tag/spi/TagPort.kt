package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.manager.spi.ManagerQueryTagPort

interface TagPort :
    QueryTagPort,
    CommandTagPort,
    ManagerQueryTagPort
