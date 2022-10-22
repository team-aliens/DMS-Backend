package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ReissueTokenUseCaseTest {

    @MockBean
    private lateinit var queryRefreshTokenPort: QueryRefreshTokenPort

    @MockBean
    private lateinit var jwtPort: JwtPort

    private lateinit var reissueTokenUseCase: ReissueTokenUseCase

    @BeforeEach
    fun setUp() {
        reissueTokenUseCase = ReissueTokenUseCase(queryRefreshTokenPort, jwtPort)
    }

    private val request = "bearer rlkemagkldamgl"
    private val userId = UUID.randomUUID()

    private val refreshToken by lazy {
        RefreshToken(
                token = request,
                userId = userId,
                authority = Authority.MANAGER,
                expirationTime = 1800
        )
    }

    @Test
    fun `토큰 재발급 성공`() {

        //given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(request))
                .willReturn(refreshToken)

        //when & then
        assertDoesNotThrow {reissueTokenUseCase.execute(request)}
    }

    @Test
    fun `리프레시 토큰을 찾을 수 없음`() {

        //given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(request))
                .willReturn(null)

        //when & then
        assertThrows<RefreshTokenNotFoundException> {
            reissueTokenUseCase.execute(request)
        }
    }

}