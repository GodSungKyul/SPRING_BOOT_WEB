package com.example.demo.model.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
// List<Article> findAll();
}

// 7주차 18페이지
// @Repository
// public interface BoardRepository extends JpaRepository<Board, Long>{
// List<Article> findAll();
// }