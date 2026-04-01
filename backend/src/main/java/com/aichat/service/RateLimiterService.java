package com.aichat.service;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RateLimiterService {

    private final Map<Long, List<Long>> requestTimestamps = new HashMap<>();

    private static final int MAX_REQUESTS = 10;
    private static final long TIME_WINDOW_MS = 60 * 1000 ; // 1 minute

    public synchronized boolean isAllowed(Long conversationId) {
        long now = System.currentTimeMillis();

        requestTimestamps.putIfAbsent(conversationId, new ArrayList<>());
        List<Long> timestamps = requestTimestamps.get(conversationId);

        // Remove old timestamps outside time window
        timestamps.removeIf(timestamp -> (now - timestamp) > TIME_WINDOW_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}