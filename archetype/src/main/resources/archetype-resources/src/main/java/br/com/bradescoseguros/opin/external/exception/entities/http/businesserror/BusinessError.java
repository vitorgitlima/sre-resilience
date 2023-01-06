package br.com.bradescoseguros.opin.external.exception.entities.http.businesserror;

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
public class BusinessError implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ErrorDataBusiness> errors;
    private MetaDataBusiness meta;

}
