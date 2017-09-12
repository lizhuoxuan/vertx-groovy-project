package com.core.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait LzxLog {

    private Logger _log

    Logger getLog() {

        if (_log == null)
            _log = LoggerFactory.getLogger(this.class)
        return _log
    }
}
