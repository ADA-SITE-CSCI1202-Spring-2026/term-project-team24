import model.SupplyItem;

public class TestDepot {

    public static void main(String[] args) {

        DepotManager depot = new DepotManager();

        System.out.println("Fuel: " + depot.getSupply(SupplyItem.FUEL));

        boolean ok = depot.hasEnough(1000, 200);
        System.out.println("Can process: " + ok);

        depot.consumeResources(1000, 200, 10000);

        System.out.println("Fuel after: " + depot.getSupply(SupplyItem.FUEL));
        System.out.println("Budget: " + depot.getBudget());
    }
}