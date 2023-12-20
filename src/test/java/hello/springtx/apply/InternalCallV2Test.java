package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;

    @Autowired
    InternalService internalService;

    @Test
    void printProxy() {
        log.info("callService class = {}", callService.getClass()); // hello.springtx.apply.InternalCallV1Test$CallService$$SpringCGLIB$$0
    }

    @DisplayName("")
    @Test
    void internalCall() {
        internalService.internal();
    }

    @DisplayName("")
    @Test
    void externalCallV2() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService() {
            return new InternalService();
        }
    }

    @Slf4j
    static class CallService {
        private final InternalService internal;

        public CallService(InternalService internal) {
            this.internal = internal;
        }

        public void external() {
            log.info("call external");
            printTxInfo();
            internal.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("tx active = {}", txActive);
        }
    }

    @Slf4j
    static class InternalService {
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("tx active = {}", txActive);
        }
    }
}
