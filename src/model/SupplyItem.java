package model;

public enum SupplyItem {
    FUEL("Jet Fuel", "L"),
    MEALS("In-flight Meals", "meals"),
    BAGGAGE_CARTS("Baggage Carts", "carts");

    private final String displayName;
    private final String unit;

    SupplyItem(String displayName, String unit) {
        this.displayName = displayName;
        this.unit = unit;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return displayName;
    }
}