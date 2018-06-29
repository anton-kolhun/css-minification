package com.devchallenge.cssminification.controller;


import com.devchallenge.cssminification.CssMinificationApplication;
import com.devchallenge.cssminification.config.CacheConfig;
import com.devchallenge.cssminification.config.MockingConfig;
import com.devchallenge.cssminification.service.CssMinificationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {CssMinificationApplication.class, MockingConfig.class})
public class CacheControllerTest {

    @LocalServerPort
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    @Qualifier("cssMinificationService")
    private CssMinificationService cssMinificationService;


    @Test
    @DirtiesContext
    public void checkCacheUsage() {
        //setup
        String url = "fake_url";
        String payload = "[\"" + url + "\"]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        //execute
        restTemplate.exchange("http://localhost:" + port + "/minify",
                HttpMethod.POST, requestEntity, Map.class);
        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/minify",
                HttpMethod.POST, requestEntity, Map.class);

        //verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(cssMinificationService, Mockito.times(1)).minifyCss(url);
    }

    @Test
    @DirtiesContext
    public void checkCacheRemoval(){
        //setup
        String url = "fake_url";
        String payload = "[\"" + url + "\"]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        //execute
        restTemplate.exchange("http://localhost:" + port + "/minify",
                HttpMethod.POST, requestEntity, Map.class);
        //reset cache
        restTemplate.getForObject("http://localhost:" + port + "/cache/" + CacheConfig.CSS_COMPRESSOR_CACHE +
                "/remove", Void.class);

        ResponseEntity<Map> response = restTemplate.exchange("http://localhost:" + port + "/minify",
                HttpMethod.POST, requestEntity, Map.class);

        //verify
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(cssMinificationService, Mockito.times(2)).minifyCss(url);
    }

}