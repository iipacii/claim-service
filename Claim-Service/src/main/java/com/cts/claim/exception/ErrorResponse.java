package com.cts.claim.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class ErrorResponse {
	private Integer statusCode;
	private String StatusMsg;
	private LocalDateTime statusDate;

}
