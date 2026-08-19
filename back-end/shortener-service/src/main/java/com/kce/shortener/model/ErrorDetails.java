package com.kce.shortener.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {
	private Date timeStamp;
	private String description;
	private String error;
}
