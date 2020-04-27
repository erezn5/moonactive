package com.moonactive.automation.logger;

import org.apache.log4j.Logger;

public class LoggerFormat {

    protected final Logger logger = Logger.getRootLogger();
    public void info(Object message){ //todo - delete duplicate methods
        logger.info(message);
    }

    public void error(Object message){
        logger.error(message);
    }

    public void error(Object message , Throwable t){
        logger.error(message , t);
    }

    public void warn(Object message){
        logger.warn(message);
    }

    public void warn(Object message , Throwable t){
        logger.warn(message , t);
    }
    public void debug(Object message){
        logger.debug(message);
    }

    public void debug(Object message , Throwable t){
        logger.debug(message , t);
    }

    public void i(String messageFormat , Object...args) {
        info(String.format(messageFormat , args));
    }

    public void e(Throwable t , String messageFormat , Object...args) {
        error(String.format(messageFormat , args) , t);
    }

    public void e(String messageFormat , Object...args) {
        error(String.format(messageFormat , args));
    }

    public void w(String messageFormat , Object...args) {
        warn(String.format(messageFormat , args));
    }

    public void w(Throwable t ,String messageFormat , Object...args) {
        warn(String.format(messageFormat , args) , t);
    }

    public void d(String messageFormat , Object...args) {
        debug(String.format(messageFormat , args));
    }

    public void d(Throwable t , String messageFormat , Object...args) {
        debug(String.format(messageFormat , args) , t);
    }

}

