package br.com.bradescoseguros.opin.interfaceadapter.controller;

import br.com.bradescoseguros.opin.businessrule.gateway.DemoSREGateway;
import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCase;
import br.com.bradescoseguros.opin.businessrule.usecase.demosre.DemoSREUseCaseImpl;
import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.domain.demosre.ExtraStatusCode;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import br.com.bradescoseguros.opin.interfaceadapter.gateway.DemoSREGatewayImpl;
import br.com.bradescoseguros.opin.interfaceadapter.mapper.DemoSREMapper;
import br.com.bradescoseguros.opin.interfaceadapter.repository.DemoSRERepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(MockitoExtension.class)
class DemoSREControllerTest {

    private static final String BASE_URL = "/api/sre/v1";

    private MockMvc mockMvc;

    @Mock
    private DemoSREUseCase useCaseMock;

    @InjectMocks
    private DemoSREController controller;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();


//        doNothing().when(useCaseMock).insertDemoSRE(any(DemoSRE.class));
//        doNothing().when(useCaseMock).updateDemoSRE(any(DemoSRE.class));
//        doNothing().when(useCaseMock).removeDemoSRE(anyInt());
//        when(useCaseMock.externalApiCall(any(ExtraStatusCode.class))).thenReturn("ok");
    }

    @Test
    @Tag("comp")
    void getDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/getDemoSRE/" + 1;
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        when(useCaseMock.getDemoSRE(anyInt())).thenReturn(demoSREMock);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).isEqualTo(demoSREMockJson);
    }

    @Test
    void insertDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/insertDemoSRE";
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        doNothing().when(useCaseMock).insertDemoSRE(any());

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(demoSREMockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
    }

    @Test
    void updateDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/updateDemoSRE";
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        String demoSREMockJson = new ObjectMapper().writeValueAsString(demoSREMock);

        doNothing().when(useCaseMock).updateDemoSRE(any());

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(demoSREMockJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(204);
    }

    @Test
    void removeDemoSRE() throws Exception {
        //Arrange
        final String url = BASE_URL + "/removeDemoSRE/" + 1;

        doNothing().when(useCaseMock).removeDemoSRE(anyInt());

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(204);
    }

    @Test
    void externalApiCall() throws Exception {
        //Arrange
        final String url = BASE_URL + "/externalApiCall/ok";
        final String response = "ok";

        when(useCaseMock.externalApiCall(any(ExtraStatusCode.class))).thenReturn(response);

        //Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andReturn();

        //Assert
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).isEqualTo(response);
    }
}