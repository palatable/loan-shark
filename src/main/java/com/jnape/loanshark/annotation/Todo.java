package com.jnape.loanshark.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target({ANNOTATION_TYPE, CONSTRUCTOR, FIELD, METHOD, PACKAGE, PARAMETER, TYPE})
@Retention(SOURCE)
public @interface Todo {

    String created();

    String author() default "Everyone";

    String description() default "[No description]";
}
