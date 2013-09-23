package com.jnape.loanshark;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Map;

public class MaxLifeResolver {

    static final String MAX_LIFE_OPTION  = "loanshark.max.life.in.days";
    static final int    DEFAULT_MAX_LIFE = 30;
    private final Map<String, String> options;
    private final Messager            messager;

    public MaxLifeResolver(ProcessingEnvironment processingEnvironment) {
        options = processingEnvironment.getOptions();
        messager = processingEnvironment.getMessager();
    }

    public int resolveMaxLifeInDays() {
        String maxLifeOptionValue = options.get(MAX_LIFE_OPTION);
        if (maxLifeOptionValue != null) {
            try {
                return Integer.parseInt(maxLifeOptionValue);
            } catch (NumberFormatException e) {
                messager.printMessage(Diagnostic.Kind.WARNING, "Error parsing " + MAX_LIFE_OPTION + ": <" + maxLifeOptionValue + "> is not an integer");
            }
        }

        return DEFAULT_MAX_LIFE;
    }
}
