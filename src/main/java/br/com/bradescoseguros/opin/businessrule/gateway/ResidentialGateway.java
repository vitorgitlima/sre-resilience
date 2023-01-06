package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResidentialGateway {

	Page<ResidentialDomain> findByProductCodes(@NonNull final Pageable pageable, @NonNull final List<String> codes);

}