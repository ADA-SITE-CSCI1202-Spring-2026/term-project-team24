package service;

import model.*;

public interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    boolean service(Aircraft aircraft, DepotManager depot);
import model.Aircraft;

interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    void serviceFlight(Aircraft aircraft);
}