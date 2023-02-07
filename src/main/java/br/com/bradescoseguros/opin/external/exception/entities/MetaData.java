package br.com.bradescoseguros.opin.external.exception.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String meta;
    private final String traceID;

}
