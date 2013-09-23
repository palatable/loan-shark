package com.jnape.loanshark;

import com.jnape.loanshark.annotation.Todo;

public class TodoFormatter {

    static final String format = "[%s] %s: %s";

    public String format(Todo todo) {
        return String.format(format, todo.created(), todo.author(), todo.description());
    }
}
