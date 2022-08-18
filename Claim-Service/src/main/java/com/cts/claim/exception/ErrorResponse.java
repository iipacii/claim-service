package com.cts.claim.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime statusDate;

}
