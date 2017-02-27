package larisa.jsf.productType;

import larisa.entity.ProductType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by home on 24.02.17.
 */
@Component("productTypeEditor")
@Scope("view")
public class ProductTypeEditor extends FileEditor<ProductType,Integer> {
    public ProductTypeEditor() {

    }

}
