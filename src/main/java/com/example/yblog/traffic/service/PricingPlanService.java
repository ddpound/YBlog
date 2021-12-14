package com.example.yblog.traffic.service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration; import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class PricingPlanService {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private String getHost(HttpServletRequest httpServletRequest){
        return httpServletRequest.getHeader("Host");
    }

    /* ---------------------- 접속 제한! ----------------- */
    public Bucket resolveBucket(HttpServletRequest httpServletRequest) {
        return cache.computeIfAbsent(getHost(httpServletRequest), this::newBucket);
    }

    // 사이즈가 커지면 늘리겠지만 그럴일은 없을듯
    private Bucket newBucket(String apiKey) {
        return Bucket4j.builder().addLimit(Bandwidth.classic(800, Refill.
                intervally(10, Duration.ofSeconds(10)))) .build(); }





}
