loan-shark [![Build Status](https://travis-ci.org/palatable/loan-shark.png?branch=master)](https://travis-ci.org/palatable/loan-shark)
==========

Debt comes at a price.

installation
------------

Add the following dependency to your `pom.xml` file:

    <dependency>
        <groupId>com.jnape.loan-shark</groupId>
        <artifactId>loan-shark</artifactId>
        <version>1.0</version>
    </dependency>

usage
-----

You can provide the date this Todo was `created` (currently in the format mm/dd/yyyy), an `author`, and a `description`:

```Java
@Todo(created="9/22/2013", author="John Napier", description="MOAR ENCAPSULATION WTF")
public class Debtor {
    public String firstName;
    public String lastName;
}
```

...but only `created` is required:

```Java
@Todo(created="9/22/2013")
public class Debtor {
    public String firstName;
    public String lastName;
}
```

Then, specify how long `@Todo`s can live in your codebase by specifying the `-Aloanshark.max.life.in.days` compiler argument:

	javac Debtor.java -Aloanshark.max.life.in.days=45

...or, if you don't specify `loanshark.max.life.in.days`, a default value of 30 will be used.

running
-------

Loan Shark uses an Annotation Processor (JSR 209) to plug in to javac, so simpy running `javac` (whether by hand, or using Maven/Ant/Gradle) should automatically invoke the Annotation Processors. In case it doesn't, check the command line arguments passed to the Java compiler and ensure you aren't passing `-proc:none`.

license
-------

_loan-shark_ is part of [palatable](http://www.github.com/palatable), which is distributed under [The MIT License](http://choosealicense.com/licenses/mit/).
