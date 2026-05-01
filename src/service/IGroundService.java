package service;

import model.*;

public interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    boolean service(Aircraft aircraft, DepotManager depot);
}