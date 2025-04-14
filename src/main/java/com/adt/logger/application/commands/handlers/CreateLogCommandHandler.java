package com.adt.logger.application.commands.handlers;

import com.adt.logger.application.commands.CreateLogCommand;
import com.adt.logger.application.events.emitters.LogEmitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateLogCommandHandler {
    private final LogEmitter logEmitter;

    public void handle(CreateLogCommand command) {
        logEmitter.emit(command.log());
    }
}
