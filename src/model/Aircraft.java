package model;

public abstract class Aircraft {

    private String flightNumber;
    private int fuelRequired;
    private int mealsRequired;
    private int revenue;
    private int turnaroundTime;

    public Aircraft(String flightNumber, int fuelRequired,
                    int mealsRequired, int revenue, int turnaroundTime) {
        this.flightNumber = flightNumber;
        this.fuelRequired = fuelRequired;
        this.mealsRequired = mealsRequired;
        this.revenue = revenue;
        this.turnaroundTime = turnaroundTime;
    }

    public String getFlightNumber()  { return flightNumber; }
    public int getFuelRequired()     { return fuelRequired; }
    public int getMealsRequired()    { return mealsRequired; }
    public int getRevenue()          { return revenue; }
    public int getTurnaroundTime()   { return turnaroundTime; }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + " | " + flightNumber
                + " | Fuel: " + fuelRequired + "L"
                + " | Meals: " + mealsRequired;
    }
}