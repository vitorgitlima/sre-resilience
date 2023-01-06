package br.com.bradescoseguros.opin.businessrule.exception.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class ResponseHomeInsuranceListEmpty implements Serializable {

    @JsonProperty("data")
    private final ResponseHomeInsuranceListDataEmpty data = new ResponseHomeInsuranceListDataEmpty();

    @JsonProperty("links")
    private final LinksEmpty links = new LinksEmpty();

    @JsonProperty("meta")
    private final MetaEmpty meta = new MetaEmpty();

    @NoArgsConstructor
    @Getter
    public static class ResponseHomeInsuranceListDataEmpty implements Serializable {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final String fakeField = null;
    }

    @NoArgsConstructor
    @Getter
    public static class LinksEmpty implements Serializable {
        @JsonProperty("self")
        private final String self = "";

        @JsonProperty("first")
        private final String first = "";

        @JsonProperty("prev")
        private final String prev = "";

        @JsonProperty("next")
        private final String next = "";

        @JsonProperty("last")
        private final String last = "";
    }

    @NoArgsConstructor
    @Getter
    public static class MetaEmpty implements Serializable {
        @JsonProperty("totalRecords")
        private final Integer totalRecords = 0;

        @JsonProperty("totalPages")
        private final Integer totalPages = 0;
    }
}


