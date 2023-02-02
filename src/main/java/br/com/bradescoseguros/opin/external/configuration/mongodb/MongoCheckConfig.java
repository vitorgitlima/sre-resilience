//package br.com.bradescoseguros.opin.external.configuration.mongodb;
//
//import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class MongoCheckConfig {
//
//    private final DemoSRERepository demoSRERepository;
//
//    @Scheduled(fixedDelay = 60000)
//    public void scheduleFixedDelayTask() {
//        log.info("Check connection mongo - " + demoSRERepository.count());
//    }
//}
