package br.com.bradescoseguros.opin.external.exception.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String meta;
    private String traceID;

}
