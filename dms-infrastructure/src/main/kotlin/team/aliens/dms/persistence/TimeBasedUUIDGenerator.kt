package team.aliens.dms.persistence

import com.fasterxml.uuid.Generators
import java.io.Serializable
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator

class TimeBasedUUIDGenerator : IdentifierGenerator {

    /**
     * 커스텀 ID를 생성하기 위해서 IdentifierGenerator 인터페이스의 generate() 메서드를 구현합니다.
     **/
    override fun generate(session: SharedSessionContractImplementor?, entity: Any?): Serializable {
        return Generators.timeBasedGenerator().generate()
    }
}