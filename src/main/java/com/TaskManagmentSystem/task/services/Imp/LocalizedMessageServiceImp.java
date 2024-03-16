package com.TaskManagmentSystem.task.services.Imp;

import com.TaskManagmentSystem.task.services.interfaces.LocalizedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
public class LocalizedMessageServiceImp implements LocalizedMessageService {

    @Autowired
    MessageSource messageSource;
    @Override
    public String getLocalizedMessage(String s) {
        // s is the messagee key
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());

    }
}
