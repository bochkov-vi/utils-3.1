package test;

import larisa.repository.ProductTypeRepository;
import larisa.service.ProductTypeService;
import org.entity3.repository.CustomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by home on 23.02.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring-config.xml")
public class ProductTypeRepositoryTest implements ApplicationContextAware {
    @Autowired
    ProductTypeRepository productTypeRepository;

    ApplicationContext applicationContext;

    @Autowired
    ProductTypeService productTypeService;

    @Test
    public void mainTest() {

       for(String name: applicationContext.getBeanNamesForType(CustomRepository.class)){
           CustomRepository customRepository = (CustomRepository) applicationContext.getBean(name);
           customRepository.findAll();
       }
    }
    @Test
    public void testService(){
        productTypeService.findAll();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}