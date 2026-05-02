package service;

import model.*;

public interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    void serviceFlight(Aircraft aircraft, Logger logger);

    @FunctionalInterface
    interface Logger {
        void log(String message);
    }
}
