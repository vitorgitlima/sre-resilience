package br.com.bradescoseguros.opin.external.exception.entities.http.gatewayerror;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class MetaDataGateway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(example = "503 SERVICE_UNAVAILABLE")
    private String meta;

}
