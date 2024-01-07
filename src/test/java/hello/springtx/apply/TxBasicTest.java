package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TxBasicTest {
    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        log.info("aop class={}", basicService.getClass()); // aop class=class hello.springtx.apply.TxBasicTest$BasicService$$SpringCGLIB$$0
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @DisplayName("")
    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    
    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void tx() {
            log.info("tx");
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("txActive = {}", txActive);
        }

        public void nonTx() {
            log.info("nonTx");
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("txActive = {}", txActive);
        }
    }
}
