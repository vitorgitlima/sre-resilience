package br.com.bradescoseguros.opin.external.exception.entities.http.internalservererror;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class InternalServerError implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(example = "1970-01-01T00:00:00Z")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(example = "500")
    private String status;

    @Schema(example = "Internal Server Error")
    private String error;

    @Schema(example = "null")
    private String message;

}
