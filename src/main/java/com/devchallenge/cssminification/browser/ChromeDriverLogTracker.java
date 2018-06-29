package com.devchallenge.cssminification.browser;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.lightbody.bmp.proxy.ProxyServer;
import net.lightbody.bmp.proxy.http.RequestInterceptor;
import net.lightbody.bmp.proxy.http.ResponseInterceptor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.util.SocketUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

class ChromeDriverLogTracker {

    private static final String REQUEST_HEADER_SEQ_NUMBER_NAME = "X-Order-Number";

    private List<LogEntry> logEntries = new ArrayList<>();

    private Lock lock = new ReentrantLock();

    private ChromeDriver chromeDriver;

    private ProxyServer proxyServer;

    private String contentTypeTrackingCriteria;


    ChromeDriverLogTracker(String contentTypeTrackingCriteria) {
        this.contentTypeTrackingCriteria = contentTypeTrackingCriteria;
        proxyServer = initProxyServer();
        chromeDriver = new ChromeDriver(initChromeOptions(proxyServer));
    }

    void trackLogs(String url) {
        chromeDriver.get(url);
    }

    private ChromeOptions initChromeOptions(ProxyServer proxyServer) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setCapability(CapabilityType.PROXY, proxyServer.seleniumProxy());
        chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        chromeOptions.setHeadless(true);
        return chromeOptions;
    }

    private ProxyServer initProxyServer() {
        ProxyServer proxyServer = new ProxyServer(SocketUtils.findAvailableTcpPort());
        proxyServer.start();
        proxyServer.remapHost("localhost", "127.0.0.1");
        proxyServer.setCaptureContent(true);
        addInterceptors(proxyServer);
        return proxyServer;
    }

    private void addInterceptors(ProxyServer proxyServer) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        proxyServer.addRequestInterceptor((RequestInterceptor) (request, context) -> request.addRequestHeader(REQUEST_HEADER_SEQ_NUMBER_NAME,
                String.valueOf(atomicInteger.getAndIncrement())));
        proxyServer.addResponseInterceptor((ResponseInterceptor)
                (response, har) -> {
                    if (response.getContentType() != null && response.getContentType().contains(contentTypeTrackingCriteria)) {
                        String orderNumber = response.getEntry().getRequest().getHeaders().stream()
                                .filter(pair -> pair.getName().equals(REQUEST_HEADER_SEQ_NUMBER_NAME))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Error occurred while intercepting server response. " +
                                        "Expected request header was absent:" + REQUEST_HEADER_SEQ_NUMBER_NAME))
                                .getValue();
                        String message = response.getEntry().getResponse().getContent().getText();
                        addLogEntry(new LogEntry(message, Integer.parseInt(orderNumber)));
                    }
                }
        );
    }

    private void addLogEntry(LogEntry logEntry) {
        logEntries.add(logEntry);
    }

    List<String> getLogs() {
        return logEntries.stream()
                .sorted(Comparator.comparing(LogEntry::getOrder))
                .map(LogEntry::getMessage)
                .collect(Collectors.toList());
    }

    boolean startLogging() {
        if (lock.tryLock()) {
            logEntries.clear();
            return true;
        }
        return false;
    }

    void destroy() {
        chromeDriver.quit();
    }


    void endLogging() {
        destroy();
        chromeDriver = new ChromeDriver(initChromeOptions(proxyServer));
        lock.unlock();
    }

    @AllArgsConstructor
    @Data
    private static class LogEntry {

        private String message;

        private int order;
    }


}