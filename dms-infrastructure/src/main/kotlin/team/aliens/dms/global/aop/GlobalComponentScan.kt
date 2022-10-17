package team.aliens.dms.global.aop

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import team.aliens.dms.global.annotation.DomainService
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import team.aliens.dms.global.annotation.UseCase

@Configuration
@ComponentScan(
    basePackages = ["team.aliens.dms"],
    includeFilters = [
        Filter(
            type = FilterType.ANNOTATION,
            classes = [
                UseCase::class,
                ReadOnlyUseCase::class,
                DomainService::class
            ]
        )
    ]
)
class GlobalComponentScan {
}