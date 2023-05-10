package team.aliens.dms.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
class AspectjConfig
