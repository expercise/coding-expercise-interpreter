package com.expercise.interpreterapi;

import com.expercise.interpreterapi.docker.InterpreterService;
import com.expercise.interpreterapi.exception.InterpreterException;
import com.expercise.interpreterapi.messages.MessageBundle;
import com.expercise.interpreterapi.request.BaseResponse;
import com.expercise.interpreterapi.request.InterpretRequest;
import com.expercise.interpreterapi.request.InterpretResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ApiController {

    private final InterpreterService interpreterService;
    private final MessageBundle messageBundle;

    @Autowired
    public ApiController(InterpreterService interpreterService, MessageBundle messageBundle) {
        this.interpreterService = interpreterService;
        this.messageBundle = messageBundle;
    }

    @PostMapping
    public InterpretResponse interpret(@Valid @RequestBody InterpretRequest interpretRequest) {
        return interpreterService.interpret(interpretRequest);
    }

    @ExceptionHandler(InterpreterException.class)
    public ResponseEntity<BaseResponse> handle(InterpreterException exception) {
        BaseResponse response = new BaseResponse();
        response.setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handle(MethodArgumentNotValidException exception) {
        BaseResponse response = new BaseResponse();
        response.setErrorMessage(messageBundle.getMessage(exception.getBindingResult().getFieldError().getCodes()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
