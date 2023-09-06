package com.hakan.basicdi.module;

import com.hakan.basicdi.annotations.PostConstruct;
import com.hakan.basicdi.annotations.Runner;
import com.hakan.basicdi.annotations.Service;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModuleTest {

    @Test
    void configure() {
        ExampleModule module = new ExampleModule();
        module.configure();
        module.create();

        ExampleService service = module.getInstance(ExampleService.class);

        assertNotNull(service);
        assertEquals(service.name, "example service");
        assertEquals(service.version, "1.0.0");
        assertEquals(module.getInstance(ExampleService.class), service);
    }

    @Test
    void checkRunners() {
        ExampleModule module = new ExampleModule();
        module.configure();
        module.create();

        assertEquals(module.name1, "example");
        assertEquals(module.name2, "example service:1.0.0");
    }



    public static class ExampleModule extends Module {

        private String name1;
        private String name2;

        @Override
        public void configure() {
            this.bind(ExampleService.class);
        }


        @Runner
        public Runnable run1() {
            return () -> this.name1 = "example";
        }

        @Runner
        public Runnable run2(ExampleService service) {
            return () -> this.name2 = service.name + ":" + service.version;
        }
    }


    @Service
    public static class ExampleService {

        public String name;
        public String version;

        @PostConstruct
        public void init() {
            this.name = "example service";
            this.version = "1.0.0";
        }
    }
}
