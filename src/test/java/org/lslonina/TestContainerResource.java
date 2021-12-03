package org.lslonina;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;

public class TestContainerResource implements QuarkusTestResourceConfigurableLifecycleManager {
    public static final PostgreSQLContainer<?> DATABASE = new PostgreSQLContainer<>("postgres:14.1");

    @Override
    public Map<String, String> start() {
        DATABASE.start();

        var confMap = new HashMap<String, String>();
        confMap.put("quarkus.datasource.jdbc.url", DATABASE.getJdbcUrl());
        confMap.put("quarkus.datasource.jdbc.username", DATABASE.getUsername());
        confMap.put("quarkus.datasource.jdbc.password", DATABASE.getPassword());
        
        return confMap;
    }

    @Override
    public void stop() {
        DATABASE.close();
    }

}
