package br.com.bradescoseguros.opin.external.exception.entities.http.businesserror;

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
public class ErrorDataBusiness implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(example = "BUSINESS_ERROR page-size")
    private String title;

    @Schema(example = "422 UNPROCESSABLE_ENTITY")
    private String code;

    @Schema(example = "Campo 'page-size' não deve ser maior que 1000.")
    private String detail;

}
