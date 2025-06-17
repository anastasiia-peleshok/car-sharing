package com.example.carsharing.repository.impl;

import com.example.carsharing.dto.car.FilterCarDto;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.Feature;
import com.example.carsharing.model.Rental;
import com.example.carsharing.repository.CustomCarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CustomCarRepositoryImpl implements CustomCarRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Car> findByFilterAndDateRange(FilterCarDto filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> cq = cb.createQuery(Car.class);
        Root<Car> car = cq.from(Car.class);
        cq.select(car).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.brands() != null && !filter.brands().isEmpty()) {
            predicates.add(car.get("brand").in(filter.brands()));
        }

        if (filter.models() != null && !filter.models().isEmpty()) {
            predicates.add(car.get("model").in(filter.models()));
        }

        if (filter.colors() != null && !filter.colors().isEmpty()) {
            predicates.add(car.get("color").in(filter.colors()));
        }

        if (filter.carTypes() != null && !filter.carTypes().isEmpty()) {
            predicates.add(car.get("carType").in(filter.carTypes()));
        }

        if (filter.years() != null && !filter.years().isEmpty()) {
            predicates.add(car.get("year").in(filter.years()));
        }

        if (filter.priceRange() != null && filter.priceRange().size() == 2) {
            BigDecimal minPrice = filter.priceRange().get(0);
            BigDecimal maxPrice = filter.priceRange().get(1);
            predicates.add(cb.between(car.get("price"), minPrice, maxPrice));
        }

        if (filter.startDate() != null && filter.endDate() != null) {
            Subquery<UUID> subquery = cq.subquery(UUID.class);
            Root<Rental> rental = subquery.from(Rental.class);
            subquery.select(rental.get("car").get("id"))
                    .where(cb.and(
                            cb.lessThan(rental.get("rentalStart"), filter.endDate()),
                            cb.greaterThan(rental.get("rentalEnd"), filter.startDate())
                    ));
            predicates.add(cb.not(car.get("id").in(subquery)));
        }

        if (filter.features() != null && !filter.features().isEmpty()) {
            Join<Car, Feature> featureJoin = car.join("features", JoinType.INNER);
            predicates.add(featureJoin.get("name").in(filter.features()));
            cq.groupBy(car.get("id"));
            cq.having(cb.equal(cb.count(featureJoin.get("id")), filter.features().size()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

}
