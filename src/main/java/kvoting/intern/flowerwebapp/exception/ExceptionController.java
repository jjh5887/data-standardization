package kvoting.intern.flowerwebapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.annotation.JsonView;

import kvoting.intern.flowerwebapp.exception.response.ErrorResponse;
import kvoting.intern.flowerwebapp.view.View;

@RestControllerAdvice("kvoting.intern.flowerwebapp")
public class ExceptionController {

	@ExceptionHandler
	@JsonView(View.Public.class)
	public ResponseEntity errorHandle(WebException webException) {
		return ResponseEntity
			.status(webException.getStatus())
			.body(ErrorResponse.builder().status(webException.getStatus())
				.message(webException.getMessage()).build());
	}
}
