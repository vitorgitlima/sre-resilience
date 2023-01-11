package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCase;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre.DemoSREDTO;
import br.com.bradescoseguros.opin.interfaceadapter.mapper.DemoSREMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sre/v1")
public class DemoSREController {

	@Autowired
	private DemoSREUseCase demoSREUseCase;

	@GetMapping("/getDemoSRE/{id}")
	public ResponseEntity<DemoSRE> getDemoSRE(@PathVariable final Integer id) {
		return ResponseEntity.ok(this.demoSREUseCase.getDemoSRE(id));
	}

	@PostMapping("/insertDemoSRE")
	public ResponseEntity<DemoSRE> insertDemoSRE(@RequestBody final DemoSREDTO payload) {
		DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);
		this.demoSREUseCase.insertDemoSRE(demoSRE);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/updateDemoSRE")
	public ResponseEntity<DemoSRE> updateDemoSRE(@RequestBody final DemoSREDTO payload) {
		DemoSRE demoSRE = DemoSREMapper.INSTANCE.mapDemoSREFrom(payload);
		this.demoSREUseCase.updateDemoSRE(demoSRE);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/removeDemoSRE/{id}")
	public ResponseEntity<DemoSRE> removeDemoSRE(@PathVariable final Integer id) {
		this.demoSREUseCase.removeDemoSRE(id);
		return ResponseEntity.noContent().build();
	}

}
