package org.tmurakam.spring.session.data.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * JavaConfig test
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=TestMongoConfiguration.class)
@WebAppConfiguration
public class JavaConfigTest {
    @Test
    public void test() {

    }
}
