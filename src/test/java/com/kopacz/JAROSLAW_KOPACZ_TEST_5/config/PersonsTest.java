package com.kopacz.JAROSLAW_KOPACZ_TEST_5.config;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.JaroslawKopaczTest5Application;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JaroslawKopaczTest5Application.class)
@TestExecutionListeners(mergeMode =
        TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        listeners = {CleanupDatabaseTestExecutionListener.class}
)
public @interface PersonsTest {
}
