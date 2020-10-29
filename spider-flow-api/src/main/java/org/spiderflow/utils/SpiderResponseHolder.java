package org.spiderflow.utils;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.io.SpiderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpiderResponseHolder {

    private static Map<String, List<SpiderResponse>> driverMap = new ConcurrentHashMap<>();

    public static void clear(SpiderContext context) {
        List<SpiderResponse> responses = driverMap.get(context.getId());
        if (responses != null) {
            for (SpiderResponse response : responses) {
                response.close();
            }
        }
        driverMap.remove(context.getId());
    }

    public synchronized static void add(SpiderContext context, SpiderResponse response) {
        driverMap.computeIfAbsent(context.getId(), k -> new ArrayList<>()).add(response);
    }
}
