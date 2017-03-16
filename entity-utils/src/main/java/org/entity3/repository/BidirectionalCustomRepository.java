package org.entity3.repository;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.AbstractPropertyAccessor;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bochkov on 16.03.17.
 */
@Repository
public class BidirectionalCustomRepository<T extends Persistable<ID>, ID extends Serializable> extends CustomRepositoryImpl<T, ID> {


    Set<BiDirect<T>> biDirects = new HashSet<>();

    public BidirectionalCustomRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        initBiDirection();
    }

    @PostConstruct
    public void initBiDirection() {
        Metamodel metamodel = em.getMetamodel();
        entityInformation.getJavaType();
        Class<T> cl = entityInformation.getJavaType();
        ManagedType<T> managedType = metamodel.managedType(cl);
        for (Attribute<T, ?> attribute1 : managedType.getDeclaredAttributes()) {
            ManagedType otherManagedType = null;
            if (attribute1.isAssociation()) {
                if (attribute1.isCollection()) {
                    PluralAttribute pa = (PluralAttribute) attribute1;
                    otherManagedType = metamodel.managedType(pa.getBindableJavaType());

                } else {
                    otherManagedType = metamodel.managedType(attribute1.getJavaType());
                }
            }
            if (otherManagedType != null) {
                for (PluralAttribute attribute2 : (Set<PluralAttribute>) otherManagedType.getDeclaredPluralAttributes()) {
                    if (attribute2.getBindableJavaType().equals(cl)) {
                        BiDirect biDirect = new BiDirect(attribute1, attribute2);
                        ContextHolder.CONTEXT.getAutowireCapableBeanFactory().autowireBean(biDirect);
                        biDirects.add(biDirect);
                    }
                }

            }
        }
    }


    <S extends T> S prepareSave(S entity) {
        try {
            if (entity != null) {
                for (BiDirect biDirect : biDirects) {
                    Collection collection = biDirect.directeRefs(entity);
                    biDirect.putRefToChild(collection, entity);
                    if (collection != null) {
                        for (Object c : collection) {
                            if ((Boolean) new DirectFieldAccessor(c).getPropertyValue("new"))
                                em.persist(c);
                            else
                                em.merge(c);
                        }
                        if (!entity.isNew()) {
                            T oldEntity = findOne(entity.getId());
                            Collection oldRefs = biDirect.directeRefs(oldEntity);
                            biDirect.removeRefFromChild(Collections2.filter(oldRefs, Predicates.not(Predicates.in(collection))), entity);
                            for (Object c : collection) {
                                em.merge(c);
                            }
                        }
                    }
                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    <S extends T> List<S> prepareSave(Iterable<S> entities) {
        return Lists.newArrayList(Iterables.transform(entities, e -> prepareSave(e)));
    }

    @Override
    public <S extends T> S save(S entity) {
        return super.save(prepareSave(entity));
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(prepareSave(entity));
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(prepareSave(entities));
    }


    static class BiDirect<T extends Persistable> {
        Attribute directAttribute;
        Attribute inverseAttribute;


        public BiDirect(Attribute directAttribute, Attribute inverseAttribute) {
            this.directAttribute = directAttribute;
            this.inverseAttribute = inverseAttribute;
        }

        public Collection directeRefs(Object entity) {
            AbstractPropertyAccessor accessor = new DirectFieldAccessor(entity);
            Object value = accessor.getPropertyValue(directAttribute.getName());
            if (directAttribute.isCollection()) {
                return (Collection) value;
            } else {
                return Lists.newArrayList(value);
            }
        }

        public void putRefToChild(Collection childs, Object ref) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            if (childs != null)
                for (Object child : childs) {
                    putRefToChild(child, ref);
                }
        }

        public void putRefToChild(Object child, Object ref) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            if (child instanceof Collection) {
                putRefToChild((Collection) child, ref);
            } else {
                AbstractPropertyAccessor accessor = new DirectFieldAccessor(child);
                if (inverseAttribute.isCollection()) {
                    Collection inverseCollection = (Collection) accessor.getPropertyValue(inverseAttribute.getName());
                    if (inverseCollection == null) {
                        inverseCollection = (Collection) inverseAttribute.getJavaType().getConstructor().newInstance();
                    }
                    if (!inverseCollection.contains(ref)) {
                        inverseCollection.add(ref);
                    }
                } else {
                    accessor.setPropertyValue(inverseAttribute.getName(), ref);
                }
            }
        }

        public void removeRefFromChild(Object child, Object ref) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            AbstractPropertyAccessor accessor = new DirectFieldAccessor(child);
            if (inverseAttribute.isCollection()) {
                Collection inverseCollection = (Collection) accessor.getPropertyValue(inverseAttribute.getName());
                if (inverseCollection != null && inverseCollection.contains(ref)) {
                    inverseCollection.remove(ref);
                }
            } else {
                accessor.setPropertyValue(inverseAttribute.getName(), null);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            BiDirect<?> biDirect = (BiDirect<?>) o;
            return Objects.equal(directAttribute, biDirect.directAttribute) &&
                   Objects.equal(inverseAttribute, biDirect.inverseAttribute);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(directAttribute, inverseAttribute);
        }
    }
}
