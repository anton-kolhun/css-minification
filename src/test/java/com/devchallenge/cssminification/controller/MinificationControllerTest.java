package com.devchallenge.cssminification.controller;

import com.devchallenge.cssminification.CssMinificationApplication;
import com.devchallenge.cssminification.config.TestWebMvcConfig;
import com.yahoo.platform.yui.compressor.CssCompressor;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {CssMinificationApplication.class, TestWebMvcConfig.class})
public class MinificationControllerTest {

    @LocalServerPort
    private String port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ResourceLoader resourceLoader;


    /**
     * Starts web container with  exposed test endpoint: '/integration-test'
     * that returns  integration-test.html page:
     * the page contains two css files (css-1 and css2)  located in test resources
     */
    @Test
    public void compressCss() throws Exception {
        //setup
        String url = "http://localhost:" + port + "/integration-test";
        String payload = "[\"" + url + "\"]";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        File css1 = resourceLoader.getResource("classpath:static/css/css-1.css").getFile();
        String compressedCss1 = compressFile(css1);
        File css2 = resourceLoader.getResource("classpath:static/css/css-2.css").getFile();
        String compressedCss2 = compressFile(css2);
        String totalResult = compressedCss1 + compressedCss2;
        String expectedResult = "{\"" + url + "\":\"" + totalResult + "\"}";

        //execute
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/minify",
                HttpMethod.POST, requestEntity, String.class);

        //verify
        Assert.assertEquals(expectedResult, response.getBody());

    }

    private String compressFile(File file) throws IOException {
        StringReader stringReader = new StringReader(FileUtils.readFileToString(file,
                Charset.defaultCharset()));
        StringWriter stringWriter = new StringWriter();
        CssCompressor cssCompressor = new CssCompressor(stringReader);
        cssCompressor.compress(stringWriter, Integer.MAX_VALUE);
        return stringWriter.toString();
    }
}