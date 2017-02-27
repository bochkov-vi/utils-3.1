package larisa.jsf.productType;

import larisa.entity.ProductType;
import larisa.jsf.DefaultDataBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by home on 24.02.17.
 */
@Component
@Scope("session")
public class ProductTypeData extends DefaultDataBean<ProductType, Integer> {

}
