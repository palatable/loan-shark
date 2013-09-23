package com.jnape.loanshark;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.HashMap;

import static com.jnape.loanshark.MaxLifeResolver.DEFAULT_MAX_LIFE;
import static com.jnape.loanshark.MaxLifeResolver.MAX_LIFE_OPTION;
import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MaxLifeResolverTest {

    @Mock private ProcessingEnvironment   processingEnvironment;
    @Mock private Messager                messager;
    private       HashMap<String, String> options;
    private       MaxLifeResolver         maxLifeResolver;

    @Before
    public void setUp() {
        options = new HashMap<String, String>();
        when(processingEnvironment.getOptions()).thenReturn(options);
        when(processingEnvironment.getMessager()).thenReturn(messager);
        maxLifeResolver = new MaxLifeResolver(processingEnvironment);
    }

    @Test
    public void readsMaxLifeInDaysFromProcessingOptions() {
        final int maxLifeInDays = 20;
        givenProcessingOption(MAX_LIFE_OPTION, maxLifeInDays + "");

        assertEquals(maxLifeInDays, maxLifeResolver.resolveMaxLifeInDays());
    }

    @Test
    public void usesDefaultMaxLifeIfNoProcessingOptionGiven() {
        assertEquals(DEFAULT_MAX_LIFE, maxLifeResolver.resolveMaxLifeInDays());
    }

    @Test
    public void outputsWarningIfMaxLifeOptionIsInvalid() {
        String invalidMaxLife = "not valid";
        givenProcessingOption(MAX_LIFE_OPTION, invalidMaxLife);

        maxLifeResolver.resolveMaxLifeInDays();

        verify(messager).printMessage(
                Diagnostic.Kind.WARNING,
                format("Error parsing %s: <%s> is not an integer", MAX_LIFE_OPTION, invalidMaxLife)
        );
    }

    @Test
    public void fallsBackTODefaultMaxLifeEvenIfOptionIsInvalid() {
        String invalidMaxLife = "not valid";
        givenProcessingOption(MAX_LIFE_OPTION, invalidMaxLife);

        assertEquals(DEFAULT_MAX_LIFE, maxLifeResolver.resolveMaxLifeInDays());
    }

    private void givenProcessingOption(String name, String value) {
        options.put(name, value);
    }
}
