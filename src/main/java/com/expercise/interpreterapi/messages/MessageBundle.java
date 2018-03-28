package com.expercise.interpreterapi.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class MessageBundle {

    private final MessageSource messageSource;

    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public MessageBundle(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    public String getMessage(String messageKey) {
        return messageSourceAccessor.getMessage(messageKey);
    }

    public String getMessage(String[] codes) {
        for (String code : codes) {
            try {
                return getMessage(code);
            } catch (NoSuchMessageException ignored) {
            }
        }
        throw new NoSuchMessageException("No message for codes:'" + Arrays.toString(codes) + "'");
    }
}
