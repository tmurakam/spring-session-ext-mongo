package org.tmurakam.spring.session.data.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JavaConfig test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestMongoConfiguration.class)
public class JavaConfigTest {
    @Test
    public void test() {

    }
}
