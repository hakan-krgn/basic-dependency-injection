package com.hakan.basicdi;

import com.hakan.basicdi.annotations.Autowired;
import com.hakan.basicdi.annotations.Component;
import com.hakan.basicdi.annotations.Service;
import com.hakan.basicdi.module.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InjectorTest {

    @Test
    void create() {
        Injector injector = Injector.of(new ExampleModule()).create();

        injector.getInstance(ExampleService.class);
        injector.getInstance(ExampleComponent.class);
    }

    @Test
    void checkVariables() {
        Injector injector = Injector.of(new ExampleModule()).create();

        ExampleService service = injector.getInstance(ExampleService.class);
        ExampleComponent component = injector.getInstance(ExampleComponent.class);

        assertEquals(service.name, "example service");
        assertEquals(service.version, "1.0.0");
        assertEquals(component.service, service);
        assertEquals(component.test(), "example service:1.0.0");
    }

    @Test
    void checkObjects() {
        Injector injector = Injector.of(new ExampleModule()).create();

        assertEquals(
                injector.getInstance(ExampleService.class),
                injector.getInstance(ExampleService.class)
        );

        assertEquals(
                injector.getInstance(ExampleComponent.class),
                injector.getInstance(ExampleComponent.class)
        );
    }



    public static class ExampleModule extends Module {

        @Override
        public void configure() {
            this.bind(ExampleService.class);
            this.bind(ExampleComponent.class);
        }
    }

    @Service
    public static class ExampleService {

        private final String name;
        private final String version;

        @Autowired
        public ExampleService() {
            this.name = "example service";
            this.version = "1.0.0";
        }
    }

    @Component
    public static class ExampleComponent {

        @Autowired
        private ExampleService service;

        public String test() {
            return this.service.name + ":" + this.service.version;
        }
    }
}
