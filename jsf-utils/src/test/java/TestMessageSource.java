import jsf.util3.service.JsfHierarchicalEntityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring-config.xml")
public class TestMessageSource {
    @Autowired
    @Qualifier("jsf-messages")
    MessageSource messageSource;

    @Test
    public void testInit() {
        System.out.println(messageSource.getMessage(JsfHierarchicalEntityService.CIRCLE_ERROR_MESSAGE, null, Locale.getDefault()));
    }
}
