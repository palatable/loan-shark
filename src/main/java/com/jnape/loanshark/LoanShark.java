package com.jnape.loanshark;

import com.jnape.dynamiccollection.lambda.MonadicFunction;
import com.jnape.dynamiccollection.list.DynamicList;
import com.jnape.loanshark.annotation.Todo;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.jnape.dynamiccollection.list.factory.DynamicListFactory.list;
import static com.jnape.loanshark.MaxLifeResolver.MAX_LIFE_OPTION;
import static java.lang.String.format;
import static org.joda.time.DateTime.now;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("com.jnape.loanshark.annotation.*")
@SupportedOptions(MAX_LIFE_OPTION)
public class LoanShark extends AbstractProcessor {

    public static final String CREATED_FORMAT = "MM/dd/yyyy";
    private MaxLifeResolver maxLifeResolver;
    private TodoFormatter   todoFormatter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        init(processingEnvironment, new MaxLifeResolver(processingEnvironment), new TodoFormatter());
    }

    synchronized void init(ProcessingEnvironment processingEnvironment, MaxLifeResolver maxLifeResolver,
                           TodoFormatter todoFormatter) {
        super.init(processingEnvironment);
        this.maxLifeResolver = maxLifeResolver;
        this.todoFormatter = todoFormatter;
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        Set<? extends Element> debtors = roundEnvironment.getElementsAnnotatedWith(Todo.class);
        List<Todo> expiredTodos = collectExpiredTodos(debtors);

        MonadicFunction<Todo, String> format = new MonadicFunction<Todo, String>() {
            @Override
            public String apply(Todo todo) {
                return todoFormatter.format(todo);
            }
        };
        MonadicFunction<Todo, ReadableInstant> byCreated = new

                MonadicFunction<Todo, ReadableInstant>() {
                    @Override
                    public ReadableInstant apply(Todo todo) {
                        return todoCreatedAsDate(todo);
                    }
                };
        DynamicList<String> errors = list(expiredTodos).sort(byCreated).map(format);

        if (!errors.isEmpty())
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    format("Found expired todos:\n%s", errors.join("\n"))
            );

        return true;
    }

    public List<Todo> collectExpiredTodos(Collection<? extends Element> debtors) {
        List<Todo> expiredTodos = new ArrayList<Todo>();

        int todoMaxLife = maxLifeResolver.resolveMaxLifeInDays();
        for (Element debtor : debtors) {
            Todo todo = debtor.getAnnotation(Todo.class);

            failIfFromTheFuture(todo);

            DateTime todoCreationDate = todoCreatedAsDate(todo);
            DateTime todoExpirationDate = todoCreationDate.plusDays(todoMaxLife);
            DateTime today = now();

            if (!today.isBefore(todoExpirationDate))
                expiredTodos.add(todo);
        }

        return expiredTodos;
    }

    private void failIfFromTheFuture(Todo todo) {
        DateTime todoCreationDate = todoCreatedAsDate(todo);

        if (todoCreationDate.isAfter(now()))
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    format(
                            "Oh really? You've created a Todo in the future, have you? How interesting for you.\n%s",
                            todoFormatter.format(todo)
                    )
            );
    }

    private DateTime todoCreatedAsDate(Todo todo) {
        return DateTime.parse(todo.created(), DateTimeFormat.forPattern(CREATED_FORMAT));
    }

}
