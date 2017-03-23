package org.entity3.repository;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bochkov on 16.03.17.
 */
@Repository
@Transactional(readOnly = true)
public class BidirectionalCustomRepository<T extends Persistable<ID>, ID extends Serializable> extends CustomRepositoryImpl<T, ID> {


    Set<BiDirect<T>> biDirects = new HashSet<>();

    public BidirectionalCustomRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) throws NoSuchFieldException {
        super(entityInformation, entityManager);
        initBiDirection();
    }

    @PostConstruct
    public void initBiDirection() throws NoSuchFieldException {
        Metamodel metamodel = em.getMetamodel();
        Class<T> currentClass = entityInformation.getJavaType();
        ManagedType<T> currentManagedType = metamodel.managedType(currentClass);

        biDirects = Sets.newHashSet(Iterables.filter(Collections2.transform(Collections2.filter(currentManagedType.getAttributes(), attribute -> attribute
                .isAssociation()), direct -> {
            BiDirect<T> result = null;
            String mappedBy = extractMappedBy(direct);
            if (Strings.isNullOrEmpty(mappedBy)) {
                mappedBy = Iterables.getFirst(Iterables.transform(Iterables.<Attribute>filter(metamodel.managedType(((Bindable) direct)
                        .getBindableJavaType()).getAttributes(), attribute ->
                        ((Bindable) attribute).getBindableJavaType().equals(currentClass) &&
                        direct.getName().equals(extractMappedBy(attribute))), attribute -> attribute.getName()), null);
            }


            if (!Strings.isNullOrEmpty(mappedBy)) {
                Attribute inverse = metamodel.managedType(((Bindable) direct).getBindableJavaType())
                        .getAttribute(mappedBy);
                result = new BiDirect(direct, inverse);
            }
            return result;
        }), Predicates.notNull()));


    }

    String extractMappedBy(Attribute attribute) {
        return Iterables.getFirst(Iterables.filter(Iterables.<Object, String>transform(Lists.newArrayList(((Field) attribute
                .getJavaMember()).getAnnotations()), anotation -> {
            if (anotation instanceof ManyToMany) {
                ManyToMany manyToMany = (ManyToMany) anotation;
                if (manyToMany.cascade().length == 0) {
                    return manyToMany.mappedBy();
                }
            } else if (anotation instanceof OneToMany) {
                OneToMany oneToMany = (OneToMany) anotation;
                if (oneToMany.cascade().length == 0 && !oneToMany.orphanRemoval()) {
                    return oneToMany.mappedBy();
                }
            }
            return (String) null;
        }), Predicates.notNull()), null);
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        return super.save(prepareSave(entity));
    }

    @Transactional
    <S extends T> S prepareSave(S entity) {
        try {
            if (entity != null) {
                for (BiDirect biDirect : biDirects) {
                    Collection collection = biDirect.directeRefs(entity);
                    biDirect.putRefToChild(collection, entity);
                    if (collection != null) {
                       /* for (Object c : collection) {
                            if ((Boolean) PropertyUtils.getProperty(c, "new")) {
                                em.persist(c);
                            } else {
                                em.merge(c);
                            }
                        }*/
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

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(prepareSave(entities));
    }

    <S extends T> List<S> prepareSave(Iterable<S> entities) {
        return Lists.newArrayList(Iterables.transform(entities, e -> prepareSave(e)));
    }

    @Override
    public void delete(ID id) {
        super.delete(id);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        super.deleteInBatch(entities);
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
    }

    @Override
    public void deleteAllInBatch() {
        super.deleteAllInBatch();
    }

    static class BiDirect<T extends Persistable> {
        Attribute directAttribute;

        Attribute inverseAttribute;


        public BiDirect(Attribute directAttribute, Attribute inverseAttribute) {
            this.directAttribute = directAttribute;
            this.inverseAttribute = inverseAttribute;
        }

        public Collection directeRefs(Object entity) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Object value = PropertyUtils.getProperty(entity, directAttribute.getName());
            if (directAttribute.isCollection()) {
                return (Collection) value;
            } else {
                return Lists.newArrayList(value);
            }
        }

        public void putRefToChild(Collection childs, Object ref) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            if (childs != null) {
                for (Object child : childs) {
                    putRefToChild(child, ref);
                }
            }
        }

        public void putRefToChild(Object child, Object ref) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            if (child instanceof Collection) {
                putRefToChild((Collection) child, ref);
            } else {
                if (inverseAttribute.isCollection()) {
                    Collection inverseCollection = (Collection) PropertyUtils.getProperty(child, inverseAttribute.getName());
                    if (inverseCollection == null) {
                        inverseCollection = (Collection) inverseAttribute.getJavaType().getConstructor().newInstance();
                    }
                    if (!inverseCollection.contains(ref)) {
                        inverseCollection.add(ref);
                    }
                } else {
                    PropertyUtils.setProperty(child, inverseAttribute.getName(), ref);
                }
            }
        }


        public void removeRefFromChild(Object child, Object ref) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            if (child instanceof Collection) {
                removeRefFromChild((Collection) child, ref);
            }
            if (inverseAttribute.isCollection()) {
                Collection inverseCollection = (Collection) PropertyUtils.getProperty(child, inverseAttribute.getName());
                if (inverseCollection != null && inverseCollection.contains(ref)) {
                    inverseCollection.remove(ref);
                }
            } else {
                PropertyUtils.setProperty(child, inverseAttribute.getName(), null);
            }
        }

        public void removeRefFromChild(Collection child, Object ref) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            for (Object o : child) {
                removeRefFromChild(o, ref);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(directAttribute, inverseAttribute);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BiDirect<?> biDirect = (BiDirect<?>) o;
            return Objects.equal(directAttribute, biDirect.directAttribute) &&
                   Objects.equal(inverseAttribute, biDirect.inverseAttribute);
        }
    }
}
