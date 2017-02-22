package org.entity3;

import java.io.Serializable;

/**
 * Created by bochkov on 25.01.17.
 */
public interface IIdable<ID extends Serializable> {
    ID getId();
}
