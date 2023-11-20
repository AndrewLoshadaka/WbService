package com.allur.wbstatserver.sybaseapi;

import com.allur.wbstatserver.model.dto.requestsDTO.SybaseRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SybaseApi {
    private final RestTemplate restTemplate;

    public List getSybaseInform(long dateFrom, long dateTo){
        String urlTemp = UriComponentsBuilder.fromHttpUrl("http://192.168.208.235:9080/sybase/all")
                .queryParam("dateFrom", dateFrom)
                .queryParam("dateTo", dateTo)
                .encode()
                .toUriString();
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);
        var result = restTemplate.exchange(urlTemp, HttpMethod.GET, httpEntity, List.class);

        return result.getBody();
    }
}
