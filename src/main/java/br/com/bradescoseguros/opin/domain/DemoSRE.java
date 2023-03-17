package br.com.bradescoseguros.opin.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("demo_sre")
@ToString
public class DemoSRE implements Serializable {

    @Id
    private Integer id;
    private String value;
}
