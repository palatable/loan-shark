package com.jnape.loanshark;

import com.jnape.loanshark.annotation.Todo;
import org.junit.Test;

import static java.lang.String.format;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static testsupport.TodoTestFactory.todoCreated;

public class TodoFormatterTest {

    @Test
    public void canFormatANormalTodo() {
        TodoFormatter todoFormatter = new TodoFormatter();

        Todo todo = todoCreated(now());
        String created = todo.created();
        String author = todo.author();
        String description = todo.description();

        assertEquals(format(TodoFormatter.format, created, author, description), todoFormatter.format(todo));
    }
}
