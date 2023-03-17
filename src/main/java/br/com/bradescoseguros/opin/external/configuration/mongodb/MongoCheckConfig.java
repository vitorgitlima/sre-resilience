package br.com.bradescoseguros.opin.external.configuration.mongodb;

import br.com.bradescoseguros.opin.interfaceadapter.repository.CrudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoCheckConfig {

    private final CrudRepository crudRepository;

    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {

        Map<String, String> countLog = new HashMap<>(Map.of(
                "Check connection mongo - obfuscated", String.valueOf(crudRepository.count())
        ));

        JSONObject countLogDetails = new JSONObject(countLog);

        log.info(String.valueOf(countLogDetails));

        log.info("Check connection mongo - " + crudRepository.count());
    }
}
