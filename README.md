# Basic Dependency Injection

Basic Dependency Injection is a library inspired by Guice that provides a simple dependency injection mechanism for
managing components in your application. The library includes popular annotations such
as `@Autowired`, `@Component`, `@PostConstruct`, `@Provide`, and `@Service`.

## Getting Started

This guide will help you get started with Basic Dependency Injection, a library that simplifies dependency injection in
your Java applications.

### Prerequisites

- Java Development Kit (JDK) 8 or above

### Installation

You can include Basic Dependency Injection in your project by adding the following Maven or Gradle dependency.

#### Maven

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.hakan-krgn</groupId>
        <artifactId>basic-dependency-injection</artifactId>
        <version>0.0.5.1</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

#### Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.hakan-krgn:basic-dependency-injection:0.0.5.1'
}
```

## Usage

### Creating a Module

A module is a collection of bindings that define how to create instances of your types. Modules use the `@Provide`
annotation to define bindings. The `@Provide` annotation is used to annotate methods that provide instances of types
that are not explicitly bound in the module. The `@Provide` annotation is also used to annotate methods that provide
instances of types that are explicitly bound in the module.

```java
public class MyModule extends Module {

    public void configure() {
        //Also you can bind repository module like this, these are
        //same things with @Provide annotation, but you don't have to
        //specify this binding if you use @Provide annotation.
        this.bind(MyRepository.class).withInstance(new MyRepository());

        // Other bindings...
        this.bind(MyService.class);
    }

    @Provide
    public MyRepository myRepository() {
        return new MyRepository();
    }
}
```

### Injecting Dependencies

You can inject dependencies into your instances by annotating constructors with the `@Autowired` annotation.
The `@Autowired` annotation is used to annotate constructors that are not explicitly bound in the module.

```java

@Service
public class MyService {

    private MyRepository myRepository;

    @Autowired
    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println(this.myRepository.get(1));
    }

    public void doSomething() {
        for (int i = 0; i < 10; i++) {
            System.out.println(this.myRepository.get(i));
        }
    }
    // Other methods...
}

public class MyRepository {

    public String get(int id) {
        return "Hello World! " + id;
    }
}
```

### Starting the Application

You can run your application by creating an instance of the `Injector` class by calling the `Injector.of()` method and
passing an instance of your module to the method. You can then use the `getInstance()` method of the `Injector` class to
get an instance of your services or components.

```java
public class Application {

    public static void main(String[] args) {
        Injector injector = Injector.of(new MyModule());

        MyService service = injector.getInstance(MyService.class);
        myService.doSomething();
    }
}
```

## License

This project is licensed under
the [MIT License.](https://github.com/hakan-krgn/basic-dependency-injection/blob/master/LICENSE)
