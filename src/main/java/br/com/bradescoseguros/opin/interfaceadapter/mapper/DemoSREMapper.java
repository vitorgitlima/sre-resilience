package br.com.bradescoseguros.opin.interfaceadapter.mapper;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre.DemoSREDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DemoSREMapper {

    @Named("mapDemoSREFrom")
    DemoSRE mapDemoSREFrom(final DemoSREDTO demoSREDTO);
}
