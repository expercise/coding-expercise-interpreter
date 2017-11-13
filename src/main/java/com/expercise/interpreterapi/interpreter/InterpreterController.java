package com.expercise.interpreterapi.interpreter;

import com.expercise.interpreterapi.exception.InterpreterException;
import com.expercise.interpreterapi.messages.MessageBundle;
import com.expercise.interpreterapi.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class InterpreterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterpreterController.class);

    private final Interpreter interpreter;
    private final MessageBundle messageBundle;

    @Autowired
    public InterpreterController(Interpreter interpreter, MessageBundle messageBundle) {
        this.interpreter = interpreter;
        this.messageBundle = messageBundle;
    }

    @PostMapping("/eval")
    public Interpreter.Response interpret(@Valid @RequestBody Interpreter.Request interpretRequest) {
        return interpreter.interpret(interpretRequest);
    }

    @ExceptionHandler(InterpreterException.class)
    public ResponseEntity<ErrorResponse> handle(InterpreterException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(exception.getMessage());

        LOGGER.error("Interpreter exception: {}", exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorMessage(messageBundle.getMessage(exception.getBindingResult().getFieldError().getCodes()));

        LOGGER.error("MethodArgumentNotValid exception: {}", exception);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
