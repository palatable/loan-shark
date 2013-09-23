package testsupport;

import com.jnape.loanshark.annotation.Todo;
import org.joda.time.DateTime;

import java.lang.annotation.Annotation;

import static com.jnape.loanshark.LoanShark.CREATED_FORMAT;

public class TodoTestFactory {

    public static Todo todoCreated(final DateTime when) {
        return new Todo() {
            @Override
            public String created() {
                return when.toString(CREATED_FORMAT);
            }

            @Override
            public String author() {
                return "Test runner";
            }

            @Override
            public String description() {
                return "Just a testable Todo";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Todo.class;
            }
        };
    }
}
