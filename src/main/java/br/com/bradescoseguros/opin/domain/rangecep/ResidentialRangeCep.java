package br.com.bradescoseguros.opin.domain.rangecep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "faixa_cep")
public class ResidentialRangeCep implements Serializable {

    @Id
    private String id;

    private String rangeId;
    private String versionCode;
    private String productCode;
    private String regionCode;
    private String postalCodeStart;
    private String postalCodeEnd;
    private String region;
    private String federateUnit;
    private String changeByUser;
    private String lastDateModified;
}