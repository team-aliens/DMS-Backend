package team.aliens.dms.global.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import team.aliens.dms.domain.auth.model.Authority.MANAGER
import team.aliens.dms.domain.auth.model.Authority.STUDENT
import team.aliens.dms.global.filter.FilterConfig

@Configuration
class SecurityConfig(
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val accessDeniedHandler: CustomAccessDeniedHandler,
    private val filterConfig: FilterConfig
) {

    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        http
            .authorizeHttpRequests { authorize -> authorize
                // /healthcheck
                .requestMatchers(HttpMethod.GET, "/").permitAll()

                // /auth
                .requestMatchers(HttpMethod.GET, "/auth/account-id").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/email").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/code").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/code").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/tokens").permitAll()
                .requestMatchers(HttpMethod.PUT, "/auth/reissue").permitAll()

                // /users
                .requestMatchers(HttpMethod.GET, "/users/password").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/users/password").hasAnyAuthority(STUDENT.name, MANAGER.name)

                // /students
                .requestMatchers(HttpMethod.GET, "/students/email/duplication").permitAll()
                .requestMatchers(HttpMethod.GET, "/students/account-id/duplication").permitAll()
                .requestMatchers(HttpMethod.GET, "/students/account-id/{school-id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/students/name").permitAll()
                .requestMatchers(HttpMethod.GET, "/students/profile").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.POST, "/students/signup").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/students/password/initialization").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/students/profile").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.DELETE, "/students").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/students/manager").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/students").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/students/{student-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/students/{student-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/students/file").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/students/file/room").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/students/file/gcn").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/students/verified-student").hasAuthority(MANAGER.name)

                // /managers
                .requestMatchers(HttpMethod.GET, "/managers/account-id/{school-id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/managers/profile").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/managers/password/initialization").permitAll()

                // /schools
                .requestMatchers(HttpMethod.GET, "/schools").permitAll()
                .requestMatchers(HttpMethod.GET, "/schools/question/{school-id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/schools/answer/{school-id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/schools/code").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/schools/question").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/schools/code").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/schools/available-features").hasAnyAuthority(MANAGER.name, STUDENT.name)

                // /notices
                .requestMatchers(HttpMethod.GET, "/notices/status").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/notices").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/notices/{notice-id}").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/notices").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/notices/{notice-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/notices/{notice-id}").hasAuthority(MANAGER.name)

                // /files
                .requestMatchers(HttpMethod.POST, "/files").permitAll()
                .requestMatchers(HttpMethod.GET, "/files/url").permitAll()

                // /meals
                .requestMatchers(HttpMethod.GET, "/meals/{date}").hasAuthority(STUDENT.name)

                // /points
                .requestMatchers(HttpMethod.GET, "/points").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.POST, "/points/options").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/points/options/{point-option-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/points/history").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/history").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/history/students/{student-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/history/students/{student-id}/recent").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/history/file").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PUT, "/points/history/{point-history-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/options").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/points/options/{point-option-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/points/history/excel").hasAuthority(MANAGER.name)

                // /templates
                .requestMatchers(HttpMethod.GET, "/templates").permitAll()
                .requestMatchers(HttpMethod.POST, "/templates").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/templates").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/templates").permitAll()

                // /tags
                .requestMatchers(HttpMethod.GET, "/tags").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/tags/{tag-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/tags").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/tags/students").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/tags/students").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/tags/{tag-id}").hasAuthority(MANAGER.name)

                // /study-rooms
                .requestMatchers(HttpMethod.GET, "/study-rooms/available-time").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.PUT, "/study-rooms/available-time").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/types").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/study-rooms/types").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PUT, "/study-rooms/seats/{seat-id}").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.DELETE, "/study-rooms/seats/{seat-id}").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.POST, "/study-rooms").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/study-rooms/{study-room-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/study-rooms/{study-room-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/{study-room-id}/students").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/{study-room-id}/managers").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/list/students").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/list/managers").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/study-rooms/types/{type-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/my").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/study-rooms/time-slots").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/study-rooms/time-slots/{time-slot-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/study-rooms/time-slots").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/study-rooms/time-slots/{time-slot-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/study-rooms/time-slots/{time-slot-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/study-rooms/students/file").hasAuthority(MANAGER.name)

                // /remains
                .requestMatchers(HttpMethod.PUT, "/remains/available-time").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PUT, "/remains/{remain-option-id}").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.POST, "/remains/options").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/remains/options/{remain-option-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/remains/my").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/remains/options").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/remains/available-time").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/remains/options/{remain-option-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/remains/status/file").hasAuthority(MANAGER.name)

                // /notifications
                .requestMatchers(HttpMethod.POST, "/notifications/token").authenticated()
                .requestMatchers(HttpMethod.GET, "/notifications").authenticated()

                // /notification
                .requestMatchers(HttpMethod.DELETE, "/notifications/{notification-of-user-id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/notifications").authenticated()
                .requestMatchers(HttpMethod.POST, "/notifications/token").authenticated()
                .requestMatchers(HttpMethod.POST, "/notifications/topic").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/notifications/topic").authenticated()
                .requestMatchers(HttpMethod.GET, "/notifications/topic").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/notifications/topic").authenticated()

                // /outings
                .requestMatchers(HttpMethod.POST, "/outings").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.POST, "/outings/types").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/outings/types/{title}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/outings/{outing-application-id}").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/outings/types").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/outings/{outing-application-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/outings/files").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/outings/my").hasAuthority(STUDENT.name)
                .requestMatchers(HttpMethod.GET, "/outings/histories").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/outings/available-time").hasAnyAuthority(STUDENT.name, MANAGER.name)
                .requestMatchers(HttpMethod.GET, "/outings/history/{outing-application-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.POST, "/outings/available-time").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.DELETE, "/outings/available-time/{outing-available-time-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/outings/available-time/{outing-available-time-id}").hasAuthority(MANAGER.name)
                .requestMatchers(HttpMethod.PATCH, "/outings/available-time/toggle/{outing-available-time-id}").hasAuthority(MANAGER.name)

                // /bugs
                .requestMatchers(HttpMethod.POST, "/bugs").hasAuthority(STUDENT.name)

                .anyRequest().denyAll()
            }
        http
            .apply(filterConfig)

        http
            .exceptionHandling { exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            }
        return http.build()
    }

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()
}
