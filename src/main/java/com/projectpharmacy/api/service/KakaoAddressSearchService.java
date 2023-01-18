package com.projectpharmacy.api.service;

import com.projectpharmacy.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressSearchService {

    //http 클라이언트 모듈이 필요함 (restTemplate)
    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Retryable(
            value = {RuntimeException.class},  //런타임 에러가 나면
            maxAttempts = 2,    // 2번 재시도
            backoff = @Backoff(delay = 2000)    //2초 딜레이
    )
    public KakaoApiResponseDto requestAddressSearch(String address) {

        //validation check
        if (ObjectUtils.isEmpty(address)) return null;

        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        // Authorization 헤더부분에 카카오 restapikey 넣어준다
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        //kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
    }

    @Recover //원래 메소드의 리턴 타입을 꼭 맞춰주기
    public KakaoApiResponseDto recover(RuntimeException e, String address) {
        log.error("All the retries failed. address = {}, error = {}", address, e.getMessage());
        return null;
    }
}
