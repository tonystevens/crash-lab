package com.github.tomakehurst.crashtest;


import com.codahale.metrics.Histogram;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CrashTestService extends Application<CrashTestConfig> {

    @Override
    public void initialize(Bootstrap<CrashTestConfig> bootstrap) {
    }

    @Override
    public void run(CrashTestConfig configuration, Environment environment) throws Exception {
        environment.jersey().register(new CrashTestResource(
                configuration.createHttpClient(environment, "wiremock-client"),
                configuration.getWireMockHost(),
                configuration.createWireMockClient()));

        Histogram histogram = environment.metrics().histogram("example-histogram");
        histogram.update(1);
        histogram.update(2);
        histogram.update(3);
        histogram.update(4);
        histogram.update(5);

    }

    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            new CrashTestService().run(new String[] { "server", "src/main/resources/crash-test.yaml" });
        } else {
            new CrashTestService().run(args);
        }

    }
}
