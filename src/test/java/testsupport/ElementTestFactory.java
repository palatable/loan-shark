package testsupport;

import com.jnape.loanshark.annotation.Todo;

import javax.lang.model.element.Element;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ElementTestFactory {

    public static Element elementWithTodo(Todo todo) {
        Element element = mock(Element.class);
        when(element.getAnnotation(Todo.class)).thenReturn(todo);
        return element;
    }
}
