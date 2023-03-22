package br.com.bradescoseguros.opin.interfaceadapter.mapper;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import br.com.bradescoseguros.opin.interfaceadapter.controller.dto.demosre.DemoSREDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DemoSREMapper {

    DemoSREMapper INSTANCE = Mappers.getMapper(DemoSREMapper.class);

    @Named("mapDemoSREFrom")
    DemoSRE mapDemoSREFrom(final DemoSREDTO demoSREDTO);
}
