package com.price.comparator.check.category.repository.query;

import com.price.comparator.check.category.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly=true)
    public List<Category> findCategoriesByActiveAndCategoryNameAndParentCategoryAndStore(Boolean activeStatus,
            String categoryName, Long parentCategoryId, Long storeId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> cq = cb.createQuery(Category.class);

        Root<Category> category = cq.from(Category.class);
        List<Predicate> predicates = new ArrayList<>();

        if (activeStatus != null){
            predicates.add(cb.equal(category.get("active"), activeStatus));
        }
        if (categoryName != null){
            predicates.add(cb.like(cb.lower(category.get("categoryName")), "%" + categoryName.toLowerCase() + "%"));
        }
        if (parentCategoryId != null){
            predicates.add(cb.equal(category.get("parentCategory"), parentCategoryId));
        }
        if (storeId != null){
            predicates.add(cb.equal(category.get("store"), storeId));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
