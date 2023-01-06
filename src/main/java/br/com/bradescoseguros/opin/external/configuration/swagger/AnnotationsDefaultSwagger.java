package br.com.bradescoseguros.opin.external.configuration.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Execução normal. A solicitação foi bem sucedida."),
        @ApiResponse(responseCode = "201", description = "Execução normal. A solicitação foi bem sucedida."),
        @ApiResponse(responseCode = "204", description = "Operação de exclusão concluída com sucesso."),
        @ApiResponse(responseCode = "400", description = "A requisição foi malformada, omitindo atributos obrigatórios, seja no payload ou através de atributos na URL."),
        @ApiResponse(responseCode = "401", description = "Cabeçalho de autenticação ausente/inválido ou token inválido"),
        @ApiResponse(responseCode = "403", description = "O token tem escopo incorreto ou uma política de segurança foi violada"),
        @ApiResponse(responseCode = "404", description = "O recurso solicitado não existe ou não foi implementado"),
        @ApiResponse(responseCode = "405", description = "O consumidor tentou acessar o recurso com um método não suportado"),
        @ApiResponse(responseCode = "406", description = "A solicitação continha um cabeçalho Accept diferente dos tipos de mídia permitidos ou um conjunto de caracteres diferente de UTF-8"),
        @ApiResponse(responseCode = "415", description = "A operação foi recusada porque o payload está em um formato não suportado pelo endpoint."),
        @ApiResponse(responseCode = "422", description = "A solicitação foi bem formada, mas não pôde ser processada devido à lógica de negócios específica da solicitação."),
        @ApiResponse(responseCode = "429", description = "A operação foi recusada, pois muitas solicitações foram feitas dentro de um determinado período ou o limite global de requisições concorrentes foi atingido"),
        @ApiResponse(responseCode = "500", description = "Ocorreu um erro no gateway da API ou no microsserviço"),
        @ApiResponse(responseCode = "503", description = "O serviço está indisponível no momento."),
        @ApiResponse(responseCode = "504", description = "O servidor não pôde responder em tempo hábil.")})
public @interface AnnotationsDefaultSwagger {
}