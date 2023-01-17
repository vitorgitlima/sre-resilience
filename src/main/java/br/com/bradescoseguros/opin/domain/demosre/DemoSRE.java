package br.com.bradescoseguros.opin.domain.demosre;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Document("demo_sre")
public class DemoSRE implements Serializable {

    @Id
    private Integer id;
    private String value;
}
