package com.co.kr.exception;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;


//Class 부분부터 작성
@Slf4j
@ControllerAdvice
public class AllExceptionHandler {
	//Request Error
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public HttpEntity<ErrorResponse> handlerBindingResultException(RequestException exception) {
		//Catch Exception
		if(exception.getException() != null) {
			Exception ex = exception.getException();
			StackTraceElement [] steArr = ex.getStackTrace();
			for(StackTraceElement ste : steArr) {
				System.out.println(ste.toString());
			}
		}
		
		//response 담기
		ErrorResponse errRes = ErrorResponse.builder()
				.result(exception.getCode().getResult())
				.resultDesc(exception.getCode().getResultDesc())
				.resDate(CommonUtils.currentTime())
				.reqNo(exception.getReqNo())
				.httpStatus(exception.getHttpStatus())
				.build();
		
		return new ResponseEntity<ErrorResponse>(errRes, errRes.getHttpStatus());
	}
	
	//DB error
	@ExceptionHandler(InternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public HttpEntity<ErrorResponse> handlerInternalServerError(RequestException exception) {
		System.out.println("=========Internal Error=========" + exception.getMessage());
		ErrorResponse errRes = ErrorResponse.builder()
				.result(exception.getCode().getResult())
				.resultDesc(exception.getCode().getResultDesc())
				.resDate(CommonUtils.currentTime())
				.reqNo(CommonUtils.currentTime())
				.build();
		
		return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	//Error Page
	@ExceptionHandler(Exception.class)
	public ModelAndView commonException(Exception e){
		e.getStackTrace();
		ModelAndView mv = new ModelAndView();
		mv.addObject("Exception", e.getStackTrace());
		mv.setViewName("commons/commonErr.html");
		return mv;
	}
}
