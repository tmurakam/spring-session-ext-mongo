import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.tmurakam.MongoSession;
import org.tmurakam.MongoSessionConfiguration;

import static org.junit.Assert.assertSame;

/**
 * Created by tmurakam on 15/08/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = MongoSessionConfiguration.class)
@WebAppConfiguration
public class MongoSessionConfigurationTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    SessionRepositoryFilter<MongoSession> filter;

    @Test
    public void getBeanByName() {
        Object filer = context.getBean("springSessionRepositoryFilter");
        assertSame(this.filter, filter);
    }

    @Test
    public void testFilter() {
    }
}
