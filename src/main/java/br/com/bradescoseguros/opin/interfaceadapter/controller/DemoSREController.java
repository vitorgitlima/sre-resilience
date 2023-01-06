package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.demosre.IDemoSREUseCase;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoSREController {

	@Autowired
	private IDemoSREUseCase demoSREUseCase;

	@GetMapping("/ok")
	public ResponseEntity<DemoSRE> ok() {
		return  ResponseEntity.ok(demoSREUseCase.getDemoSRE());
	}

}
