package br.com.bradescoseguros.opin.external.exception.entities.http.gatewayerror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class GatewayError implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ErrorDataGateway> errors;
    private MetaDataGateway meta;

}
