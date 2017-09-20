public class lab_one {
    public static void main(String[] args) {
//		Purchase two gas stations, and set them up
        gasStation a = new gasStation(2.29, 2.49);
        gasStation b = new gasStation(2.09, 2.19);

//		Check master fuel storage for both stations
        System.out.println("Station A is starting with " + a.getCapacity() + " gallons of Super and Regular fuel.");
        System.out.println("Station B is starting with " + b.getCapacity() + " gallons of Super and Regular fuel.");

        System.out.println("\n");

//		Register sales for the first gas station; check capacity
        a.sellSuper(8);
        a.sellRegular(10);
        System.out.println("Station A sold 8 gallons of Super and 10 gallons of Regular fuel and thus...");
        System.out.println("...has " + a.getSuperCapacity() + " gallons of Super and " + a.getRegularCapacity() + " gallons of Regular fuel left.");

//		Register sales for the second gas station
        b.sellSuper(11);
        b.sellRegular(12);
        System.out.println("Station B sold 11 gallons of Super and 12 gallons of Regular fuel and thus...");
        System.out.println("...has " + b.getSuperCapacity() + " gallons of Super and " + b.getRegularCapacity() + " gallons of Regular fuel left.");

        System.out.println("\n");

//		Compare and print out most profitable gas station
        if (a.hasMoreProfit(b))
            System.out.println("Station A was more profitable! Accruing approx. $" + a.getSales() + " in sales.");
        else
            System.out.println("Station B was more profitable! Accruing approx. $" + b.getSales() + " in sales.");

//		Waste away profits on a chain of 10 gasStations
        gasStation[] myStations = new gasStation[10];
        for (int i = 0; i < 10; i++) {
//			Price them the same because they're all in the same neighborhood
            myStations[i] = new gasStation(2.19, 2.39);
//			Sell an indiscriminate amount of gas, regardless of what each customer asked for
            myStations[i].sellRegular(Math.random() * 20 + 1);
            myStations[i].sellSuper(Math.random() * 20 + 1);
        }

//		Find out profits
        gasStation highest = myStations[0];
        for (int i = 0; i < 10; i++)
            if (myStations[i].hasMoreProfit(highest))
                highest = myStations[i];
        System.out.println("\nThe highest total chain sales is $" + highest.getSales());
    }
}

class gasStation {
    private double regularPrice;
    private double superPrice;
    private double regularCapacity;
    private double superCapacity;
    private double sales;
    private boolean priceGouged;

    public gasStation(double r, double s) {
        regularPrice = r;
        superPrice = s;
        regularCapacity = 1000;
        superCapacity = 1000;
        priceGouged = false;
        sales = 0;
    }

    public void sellRegular(double gallons) {
        if (gallons > regularCapacity) {
            System.out.println("Sorry, not enough Regular gas available!");
        } else {
            if (!priceGouged && regularCapacity + superCapacity < 200) {
                gouge();
                priceGouged = true;
            }
            sales += regularPrice * gallons;
            regularCapacity -= gallons;
        }
    }

    public void sellSuper(double gallons) {
        if (gallons > superCapacity) {
            System.out.println("Sorry, not enough Super gas available!");
        } else {
            if (!priceGouged && regularCapacity + superCapacity < 200) {
                gouge();
                priceGouged = true;
            }
            sales += superPrice * gallons;
            superCapacity -= gallons;
        }
    }

    public double getRegularCapacity() {
        return regularCapacity;
    }

    public double getSuperCapacity() {
        return superCapacity;
    }

    public double getCapacity() {
        return superCapacity + regularCapacity;
    }

    public double getSales() {
        return Math.round(sales * 100.0) / 100.0;
    }

    public boolean hasMoreProfit(gasStation other) {
        return sales > other.getSales();
    }

    private void gouge() {
        superPrice *= 2;
        regularPrice *= 2;
    }
}
