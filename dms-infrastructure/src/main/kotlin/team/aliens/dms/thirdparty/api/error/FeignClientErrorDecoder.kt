package team.aliens.dms.thirdparty.api.error

import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import team.aliens.dms.thirdparty.api.exception.OtherServerBadRequestException
import team.aliens.dms.thirdparty.api.exception.OtherServerForbiddenException
import team.aliens.dms.thirdparty.api.exception.OtherServerUnauthorizedException

class FeignClientErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String, response: Response): FeignException {
        if (response.status() >= 400) {
            when (response.status()) {
                401 -> throw OtherServerUnauthorizedException
                403 -> throw OtherServerForbiddenException
                else -> throw OtherServerBadRequestException
            }
        }

        return FeignException.errorStatus(methodKey, response)
    }
}
