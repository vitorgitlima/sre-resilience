package br.com.bradescoseguros.opin.businessrule.messages;

import br.com.bradescoseguros.opin.dummy.ObjectRandomUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageSourceServiceImplTest {

    private static final String VALID_MESSAGE = ObjectRandomUtil.nextString(10);
    private static final String VALID_KEY = ObjectRandomUtil.nextString(5);

    @Mock
    private MessageSource mockMessageSource;

    @InjectMocks
    private MessageSourceServiceImpl messageSourceServiceImpl;

    @Test
    @Tag("unit")
    void getMessage_withCorrectMessageKey_shouldBeReturnCorrectMessage() {

        //Arrange
        when(mockMessageSource.getMessage(VALID_KEY, null, LocaleContextHolder.getLocale()))
                .thenReturn(VALID_MESSAGE);

        //Act
        String message = messageSourceServiceImpl.getMessage(VALID_KEY);

        //Asserts
        assertThat(message).isNotEmpty().isEqualTo(VALID_MESSAGE);
        verify(mockMessageSource).getMessage(VALID_KEY, null, LocaleContextHolder.getLocale());
    }

    @Test
    @Tag("unit")
    void getMessage_withInvalidKey_shouldBeNull() {

        //Act
        assertThat(messageSourceServiceImpl.getMessage(" ")).isNull();

        //Asserts
        verify(mockMessageSource).getMessage(" ", null, LocaleContextHolder.getLocale());
    }

    @Test
    @Tag("unit")
    void getMessage_withCorrectMessageKeyAndQtdArgs_shouldBeReturnCorrectMessage() {

        //Arrange
        final Object[] args = {"abc"};
        when(mockMessageSource.getMessage(VALID_KEY, args, LocaleContextHolder.getLocale()))
                .thenReturn(VALID_MESSAGE);

        //Act
        String message = messageSourceServiceImpl.getMessage(VALID_KEY, args);

        //Asserts
        assertThat(message).isNotEmpty().isEqualTo(VALID_MESSAGE);
        verify(mockMessageSource).getMessage(VALID_KEY, args, LocaleContextHolder.getLocale());
    }

    @Test
    @Tag("unit")
    void getMessage_withCorrectMessageKeyButIncorrectLocale_shouldBeNoSuchMessageException() {

        //Arrange
        final Object[] args = {"abc"};
        when(mockMessageSource.getMessage(VALID_KEY, args, LocaleContextHolder.getLocale()))
                .thenThrow(NoSuchMessageException.class);

        //Act
        assertThrows(NoSuchMessageException.class, () -> messageSourceServiceImpl.getMessage(VALID_KEY, args));

        //Asserts
        verify(mockMessageSource).getMessage(VALID_KEY, args, LocaleContextHolder.getLocale());
        verifyNoMoreInteractions(mockMessageSource);
    }
}