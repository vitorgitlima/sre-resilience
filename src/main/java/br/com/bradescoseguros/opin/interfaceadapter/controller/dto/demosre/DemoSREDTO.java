package br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
public class DemoSREDTO {

	private Integer id;
	private String value;
}
