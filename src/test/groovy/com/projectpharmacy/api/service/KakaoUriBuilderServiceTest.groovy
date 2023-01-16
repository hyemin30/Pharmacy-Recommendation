package com.projectpharmacy.api.service

import spock.lang.Specification

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

//Specification 상속해야 한다
class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService

    // beforeEach
    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "buildUriByAddressSearch - 한글 파라미터의 경우 정상적으로 인코딩"() {
        given:
        String address = "서울 성북구"
        def charset = StandardCharsets.UTF_8

        when: //def로 동적으로 타입을 선언해도 된다
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodeResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodeResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 성북구"

    }
}
