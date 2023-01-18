package com.projectpharmacy.pharmacy.service;

import com.projectpharmacy.api.dto.DocumentDto;
import com.projectpharmacy.api.dto.KakaoApiResponseDto;
import com.projectpharmacy.api.service.KakaoAddressSearchService;
import com.projectpharmacy.direction.entity.Direction;
import com.projectpharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacy(String address) { //고객이 입력한 주소를 파라미터로 받음

        //주소를 위치기반 데이터로 변환한다
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        //응답값에 대한 유효성 검사
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[KakaoAddressSearchService recommendPharmacy fail] Input address = {}", address);
            return;
        }

        //위도 경도 정보를 가져옴
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        //거리계산 알고리즘을 이용한 약국 찾기
        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        directionService.saveAll(directionList);
    }

}
