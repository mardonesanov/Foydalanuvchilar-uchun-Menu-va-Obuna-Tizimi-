package com.example.demo.repository;

import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByUserId(Long userId);

    List<Menu> findByUserIdIn(List<Long> userIds);

    List<Menu> findByUser(User user);  // Get all menus for a user

    List<Menu> findAllByUserId(Long userId);

    @Query("SELECT m FROM Menu m WHERE m.user.id = :userId AND (LOWER(m.productName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Menu> searchMenusByUserIdAndQuery(@Param("userId") Long userId, @Param("query") String query);
}