package team.aliens.dms.global.config

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Method

@Configuration
class CustomKeyGenerator : KeyGenerator {

    //메서드 명을 사용해 key를 만들어준다.
    override fun generate(target: Any, method: Method, vararg params: Any): Any {
        return method.name + SimpleKeyGenerator.generateKey(*params)
    }

    @Bean
    fun keyGenerator(): KeyGenerator {
        return CustomKeyGenerator()
    }
}
