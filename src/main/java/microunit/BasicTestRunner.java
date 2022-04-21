package microunit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for running unit tests without support for expected exceptions.
 */
public class BasicTestRunner extends TestRunner {
    public Logger log = LogManager.getLogger();

    /**
     * Creates a {@code BasicTestRunner} object for executing the test methods
     * of the class specified.
     *
     * @param testClass the class whose test methods will be executed
     */
    public BasicTestRunner(Class<?> testClass) {
        super(testClass);
    }

    @Override
    public void invokeTestMethod(Method testMethod, Object instance, TestResultAccumulator results)
            throws IllegalAccessException {
        try {
            //for testing
            log.error("error");
            log.warn("warn");
            log.info("info");
            log.debug("debug");
            log.trace("trace");
        //----------------------------------------
            log.info("testing starts");
            testMethod.invoke(instance);
            results.onSuccess(testMethod);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            log.error("underlying method(method called using Reflection) throws an exception.");
            if (cause instanceof AssertionError) {
                log.warn("something unexpected failure");
                results.onFailure(testMethod);
            } else {
                log.error("Oops!something unexpected error");
                results.onError(testMethod);
            }
        }
    }

    // CHECSTYLE:OFF
    public static void main(String[] args) throws Exception {
        Class<?> testClass = Class.forName(args[0]);
        new BasicTestRunner(testClass).runTestMethods();
    }
}
