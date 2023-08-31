package com.hakan.basicdi.module;

import com.hakan.basicdi.annotations.PostConstruct;
import com.hakan.basicdi.annotations.Provide;
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



    public static class ExampleModule extends Module {

        @Override
        public void configure() {

        }

        @Provide
        public ExampleService exampleService() {
            return new ExampleService();
        }
    }


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