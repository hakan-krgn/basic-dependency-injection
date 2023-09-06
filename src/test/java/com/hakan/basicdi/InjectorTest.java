package com.hakan.basicdi;

import com.hakan.basicdi.annotations.Autowired;
import com.hakan.basicdi.annotations.Component;
import com.hakan.basicdi.annotations.Service;
import com.hakan.basicdi.module.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InjectorTest {

    private final Injector injector = Injector.of(new ExampleModule()).create();

    @Test
    void create() {
        assertNotNull(this.injector.getInstance(ExampleService.class));
        assertNotNull(this.injector.getInstance(ExampleComponent.class));
    }

    @Test
    void checkVariables() {
        ExampleService service = this.injector.getInstance(ExampleService.class);
        ExampleComponent component = this.injector.getInstance(ExampleComponent.class);

        assertNotNull(service);
        assertNotNull(component);

        assertEquals(service.name, "example service");
        assertEquals(service.version, "1.0.0");
        assertEquals(component.service, service);
        assertEquals(component.test(), "example service:1.0.0");
    }

    @Test
    void checkObjects() {
        assertEquals(
                this.injector.getInstance(ExampleService.class),
                this.injector.getInstance(ExampleService.class)
        );

        assertEquals(
                this.injector.getInstance(ExampleComponent.class),
                this.injector.getInstance(ExampleComponent.class)
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
