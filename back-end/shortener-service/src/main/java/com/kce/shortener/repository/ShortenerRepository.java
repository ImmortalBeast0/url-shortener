package com.kce.shortener.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kce.shortener.entity.URL;

@Repository
public interface ShortenerRepository extends JpaRepository<URL, Long> {

	Optional<URL> findByShortCode(String shortCode);

	boolean existsByShortCode(String shortCode);
}
