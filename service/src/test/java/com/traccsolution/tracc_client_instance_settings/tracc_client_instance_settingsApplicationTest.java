package com.traccsolution.tracc_client_instance_settings;

import com.traccsolution.tracc_client_instance_settings.extension.TestContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith({TestContainerExtension.class})
class tracc_client_instance_settingsApplicationTest {
    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }
}
