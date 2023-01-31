package br.com.bradescoseguros.opin.businessrule.validator;

import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import br.com.bradescoseguros.opin.dummy.DummyObjectsUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DemoSREValidatorTest {

    @InjectMocks
    private DemoSREValidator validator;

    @Test
    @Tag("unit")
    void execute() {
        DemoSRE demoSREMock = DummyObjectsUtil.newInstance(DemoSRE.class);
        validator.execute(demoSREMock);
    }
}