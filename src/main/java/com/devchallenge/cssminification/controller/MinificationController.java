package com.devchallenge.cssminification.controller;

import com.devchallenge.cssminification.service.AsyncCssMinificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@RequestMapping("minify")
public class MinificationController {


    @Autowired
    private AsyncCssMinificationService asyncCssMinificationService;

    @RequestMapping
    public Map<String, String> compressCss(@RequestParam(value = "noCache", required = false) boolean noCache,
                                           @RequestBody List<String> urls) throws Exception {
        Map<String, Future<String>> results = new HashMap<>();
        for (String url : urls) {
            Future<String> result = asyncCssMinificationService.minifyCss(url, noCache);
            results.put(url, result);
        }

        Map<String, String> totalResult = new HashMap<>();

        for (Map.Entry<String, Future<String>> entry : results.entrySet()) {
            totalResult.put(entry.getKey(), entry.getValue().get());
        }

        return totalResult;
    }


}
