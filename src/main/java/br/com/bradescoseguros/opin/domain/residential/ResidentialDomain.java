package br.com.bradescoseguros.opin.domain.residential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "produtos")
public class ResidentialDomain implements Serializable {

	@Id
	private String id;
	private String brandName;
	private String companyName;
	private String cnpjNumber;
	private ProductDomain product;

}