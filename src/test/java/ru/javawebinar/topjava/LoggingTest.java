package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.util.StopWatch;

public class LoggingTest implements TestRule {

    private StopWatch stopWatch = new StopWatch();
    public static StringBuilder log = new StringBuilder("\nLogging test's methods: \n");

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                stopWatch.start();
                base.evaluate();
                stopWatch.stop();
                String rowLog = "Test method " + description.getMethodName() + " running:  "+ stopWatch.getTotalTimeMillis()+" ms";
                System.out.println(rowLog);
                log.append(rowLog + "\n");
            }
        };
    }
}
