package com.adt.logger.application.commands;

import com.adt.logger.domain.entities.Log;

public record CreateLogCommand(
        Log log
) {
}
