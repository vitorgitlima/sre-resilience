package br.com.bradescoseguros.opin.external.exception.entities.http.badrequesterror;

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
public class ErrorDataBadRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(example = "INVALID_PARAMETER page")
    private String title;

    @Schema(example = "400 BAD_REQUEST")
    private String code;

    @Schema(example = "Campo 'page' deve ser maior ou igual a um.")
    private String detail;

}
