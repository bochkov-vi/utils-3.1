package larisa.service.impl;

import larisa.entity.ProductType;
import larisa.service.ProductTypeService;
import org.springframework.stereotype.Service;

/**
 * Created by home on 25.02.17.
 */
@Service("productTypeService")
public class ProductTypeServiceImpl extends DefaultEntityServiceImpl<ProductType, Integer> implements ProductTypeService {
    public ProductTypeServiceImpl() {
        super("id", "name");
    }
}
