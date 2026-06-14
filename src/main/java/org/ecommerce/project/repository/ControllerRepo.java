package org.ecommerce.project.repository;

import org.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControllerRepo extends JpaRepository<Category,Long> {
}
