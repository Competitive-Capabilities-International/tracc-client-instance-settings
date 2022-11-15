package com.traccsolution.scaffold.extension;

import com.traccsolution.scaffold.extension.containers.PostgresTestContainer;
import com.traccsolution.scaffold.extension.containers.RedisTestContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author KeeshanReddy
 */
@Slf4j
public class TestContainerExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        RedisTestContainer.initialize();
        PostgresTestContainer.initialize();
    }
}
