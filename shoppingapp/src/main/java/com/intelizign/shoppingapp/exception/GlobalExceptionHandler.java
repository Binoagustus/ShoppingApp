package com.intelizign.shoppingapp.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.intelizign.shoppingapp.response.Response;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = ShoppingException.class)
	public Response handleShoppingAppException(ShoppingException exception) {
		return new Response(exception.getMessage());
	}
}
