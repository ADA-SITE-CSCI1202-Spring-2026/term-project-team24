package service;

import model.Aircraft;

interface IGroundService {
    boolean canProcess(Aircraft aircraft);
    void serviceFlight(Aircraft aircraft);
}