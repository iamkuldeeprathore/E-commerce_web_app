package org.ecommerce.project.repository;

import org.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ControllerRepo extends JpaRepository<Category,Long> {
    Optional<Category> findByCategoryName(String categoryName);
}
