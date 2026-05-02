package service;

import model.*;

public interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    void service(Aircraft aircraft, Logger logger);

    @FunctionalInterface
    interface Logger {
        void log(String message);
    }
}
