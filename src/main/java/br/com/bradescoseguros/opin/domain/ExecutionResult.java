package br.com.bradescoseguros.opin.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExecutionResult<T> {

    private T object;
    private ErrorEnum errorType;
    private String errorMessage;
}
