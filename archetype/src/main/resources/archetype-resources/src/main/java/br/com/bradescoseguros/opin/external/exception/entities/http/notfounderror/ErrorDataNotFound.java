package br.com.bradescoseguros.opin.external.exception.entities.http.notfounderror;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ErrorDataNotFound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(example = "NOT_FOUND Para a Marca informada, não possui o recurso solicitado")
    private String title;

    @Schema(example = "404 NOT_FOUND")
    private String code;

    @Schema(implementation = ObjectUtils.Null.class)
    private String detail;

}
