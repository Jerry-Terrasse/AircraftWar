package edu.hitsz.aircraft;

public interface AircraftFactory {
    AbstractAircraft createAircraft(int x, int y, int vx, int vy);
}
