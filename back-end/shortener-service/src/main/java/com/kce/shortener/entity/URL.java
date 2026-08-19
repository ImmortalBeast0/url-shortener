package com.kce.shortener.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "urls")
public class URL {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "short_code", nullable = false, unique = true)
	private String shortCode;

	@Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
	private String originalUrl;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "click_count")
	private Long clickCount;
}
