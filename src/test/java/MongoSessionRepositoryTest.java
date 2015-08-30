import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.tmurakam.MongoSessionConfiguration;
import org.tmurakam.MongoSessionRepository;

import static org.junit.Assert.*;

/**
 * Session repository test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = MongoSessionConfiguration.class)
public class MongoSessionRepositoryTest {
    @Autowired
    MongoSessionRepository repository;

    @Test
    public void test() {
        assertNotNull(repository);
    }
}
