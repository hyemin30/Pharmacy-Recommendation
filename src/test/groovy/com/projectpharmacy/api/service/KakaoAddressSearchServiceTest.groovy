package com.projectpharmacy.api.service

import com.projectpharmacy.AbstractIntegrationContainerBaseTest
import com.projectpharmacy.api.dto.KakaoApiResponseDto
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService

    def "address 파라미터가 null이면, requestAddressSearch 메소드는 null 리턴한다"() {
        given:
        def address = null

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 유효하면 requestAddressSearch 메소드는 정상적으로 document를 반환한다"() {
        given:
        def address = "서울 성북구 종암로 10길"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentList.size() > 0
        result.metaDto.totalCount > 0
        result.documentList.get(0).addressName != null
    }
}
