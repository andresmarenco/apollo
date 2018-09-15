package com.apolloframework.dataaccess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.apolloframework.query.FilterBuilder;
import com.apolloframework.query.Filters;

/**
 * Extension of {@link PagingAndSortingRepository} to provide integration with JPA.
 * Also implements methods to use with the {@link FilterBuilder} (as defined in the query package)
 * @author amarenco
 *
 * @param <T> the type of data of the repository
 * @param <ID> the type of the ID of each record in the repository
 */
public abstract class AbstractJPARepository<T, ID extends Serializable> implements PagingAndSortingRepository<T, ID> {

    /** The generic type T used for the current repository */
    protected final Class<T> repositoryType;

    /** The entity manager factory */
    @Autowired
    protected EntityManagerFactory entityManagerFactory;
    
    /** The conversion service */
    @Autowired
    protected ConversionService conversionService;

    
    /**
     * Default constructor
     */
    @SuppressWarnings("unchecked")
    public AbstractJPARepository() {
        this.repositoryType = (Class<T>) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractJPARepository.class)[0];
    }
    
    
    /**
     * Creates a AutoCloseable {@link EntityManager} using the current {@link EntityManagerFactory}
     * @return {@link CloseableEntityManager} instance
     */
    protected CloseableEntityManager createEntityManager() {
        return new CloseableEntityManager(entityManagerFactory.createEntityManager());
    }
    
    @Override
    public <S extends T> S save(S entity) {
        try(CloseableEntityManager entityManager = this.createEntityManager()) {
            entityManager.unwrap(Session.class).save(entity);
        }
        
        return entity;
    }
    
    
    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(e -> result.add(save(e)));
        
        return result;
    }
    
    
    @Override
    public void delete(T entity) {
        try(CloseableEntityManager entityManager = this.createEntityManager()) {
            entityManager.remove(entity);
        }
    }
    
    
    @Override
    public void delete(ID id) {
        delete(this.findOne(id));
    }
    
    
    @Override
    public void delete(Iterable<? extends T> entities) {
        entities.forEach(e -> delete(e));
    }
    
    
    @Override
    public long count() {
        return this.count((FilterBuilder)null);
    }




    /**
     * Returns the number of entities available given the specified filters.
     * @param filters the filters to apply
     * @return the number of entities
     */
    public long count(FilterBuilder filters) {
        long total = 0;
        
        try(CloseableEntityManager entityManager = createEntityManager())
        {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
            Root<T> root = cq.from(repositoryType);
            cq.select(criteriaBuilder.count(root));
            
            if(filters != null) {
                cq.where(filters.convertToPredicate(criteriaBuilder, root));
            }
            
            total = entityManager.createQuery(cq).getSingleResult();
        }
        
        return total;
    }
    
    
    
    
    @Override
    public boolean exists(ID id) {
        return this.count(new FilterBuilder(Filters.equals("id", id))) == 1;
    }
    
    
    
    
    @Override
    public Iterable<T> findAll() {
        return this.findAll((FilterBuilder)null);
    }
    
    
    
    
    @Override
    public Page<T> findAll(Pageable pageRequest) {
        return this.findAll(pageRequest, null);
    }
    
    
    
    
    @Override
    public Iterable<T> findAll(Sort sort) {
        return this.findAll(sort, null);
    }



    /**
     * Returns all entities filtered by the given options.
     * @param filters the filters to apply
     * @return all entities filtered by the given options
     */
    public Iterable<T> findAll(FilterBuilder filters) {
        return this.findAll((Pageable)null, filters).getContent();
    }




    /**
     * Returns all entities sorted by the given options.
     * @param sort the definition of the sort
     * @param filters the filters to apply
     * @return all entities sorted by the given options
     */
    public Iterable<T> findAll(Sort sort, FilterBuilder filters) {
        return this.findAll(new PageRequest(0, Integer.MAX_VALUE, sort), filters).getContent();
    }




    /**
     * Returns a {@link Page} of entities meeting the given filters and the paging restriction provided in the {@code Pageable} object.
     * @param pageRequest the definition of the page or <code>null</code> for no pagination
     * @param filters the filters to apply
     * @return a page of entities
     */
    public Page<T> findAll(Pageable pageRequest, FilterBuilder filters) {
        Page<T> page = null;
        
        try(CloseableEntityManager entityManager = createEntityManager())
        {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = criteriaBuilder.createQuery(repositoryType);
            Root<T> root = cq.from(repositoryType);
            
            if(filters != null) {
                cq.where(filters.convertToPredicate(criteriaBuilder, root));
            }

            // Then, check pagination (if any)
            if(pageRequest != null) {
                // Add sorting (if any)
                if(pageRequest.getSort() != null) {
                    for(Order order : pageRequest.getSort()) {
                        Path<T> path = root.get(order.getProperty());
                        if(order.isAscending())
                            cq.orderBy(criteriaBuilder.asc(path));
                        else
                            cq.orderBy(criteriaBuilder.desc(path));
                    }
                }
                
                // Create the query
                TypedQuery<T> q = entityManager.createQuery(cq);

                // Add the pagination
                if(pageRequest.getOffset() != 0)
                    q.setFirstResult(pageRequest.getOffset());

                if((pageRequest.getPageSize() != 0) && (pageRequest.getPageSize() != Integer.MAX_VALUE))
                    q.setMaxResults(pageRequest.getPageSize());
                
                
                page = new PageImpl<>(
                        q.getResultList(),
                        pageRequest,
                        this.count(filters));
            } else {
                page = new PageImpl<>(entityManager.createQuery(cq).getResultList());
            }
        }

        return page;
    }
    
    
    
    /**
     * Returns all instances of the type with the given IDs.
     * @param ids the IDs to find
     * @param sort the definition of the sort
     * @return all entities of the type with the given IDs sorted by the given options
     */
    public Iterable<T> findAll(Iterable<ID> ids, Sort sort) {
        return this.findAll(
                new PageRequest(0, Integer.MAX_VALUE, sort),
                Filters.in("id", ids).toBuilder()).getContent();
    }
    
    
    
    
    @Override
    public Iterable<T> findAll(Iterable<ID> ids) {
        return this.findAll(ids, null);
    }
    
    
    
    
    @Override
    public T findOne(ID id) {
        T result;
        
        try(CloseableEntityManager entityManager = createEntityManager())
        {
            result = entityManager.find(repositoryType, id);
        }
        
        return result;
    }
}
