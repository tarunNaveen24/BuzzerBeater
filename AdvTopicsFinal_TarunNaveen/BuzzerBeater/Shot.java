package BuzzerBeater;

public class Shot {
    Player offense;
    Player defense;
    double hoopDistance;
    double defenderDistance;
    double shotMeter;

    public Shot(double shotPct, Player offense, Player defense, double[] offenseLoc, double[] defenseLoc) {
        this.shotMeter = shotPct;
        this.offense = offense;
        this.defense = defense;
        double[] hoopLoc = {265, 475};
        this.hoopDistance = Shot.calculateDistance(offenseLoc, hoopLoc);
        this.defenderDistance = Shot.calculateDistance(offenseLoc, defenseLoc);
    }

    public double calculateProbability() {
        if (shotMeter > 1) {
            shotMeter = 2 - shotMeter;
        } if (shotMeter > 2) {
            return 0;
        } if (shotMeter > 0.98 && shotMeter < 1.02) {
            return 1;
        }

        if (this.defenderDistance > 100) {
            defenderDistance = 100; //considered an open shot if the defender is at least this far away
        } else if (this.defenderDistance < 10) {
            return 0.05;
        } else if (this.defenderDistance > 150) {
            return Attributes.shooting.get(this.offense.getName());
        }

        double percentOpen = (defenderDistance / 100.0);
        //shot percentage * player skill * percent open - (0.1 * defense skill)
        if (hoopDistance > 400) {
            return ( (shotMeter) * (percentOpen) * ( Attributes.shooting.get(offense.getName()) / 100.0 ) ) - 
                    ( ( 0.1 * Attributes.odefense.get(defense.getName() ) / 100 ) );
        } else if (hoopDistance > 100 & hoopDistance < 400) {
            return ( (shotMeter) * (percentOpen) * ( Attributes.midrange.get(offense.getName()) / 100.0 ) ) - 
                    ( ( 0.1 * Attributes.idefense.get(defense.getName() ) / 100 ) );
        } else {
            return ( (shotMeter) * (percentOpen) * ( Attributes.inside.get(offense.getName()) / 100.0 ) ) - 
                    ( ( 0.1 * Attributes.idefense.get(defense.getName() ) / 100 ));
        }
    }

    public static double calculateDistance(double[] point1, double[] point2) {
        return Math.sqrt(Math.pow(point2[0] - point1[0], 2) + Math.pow(point2[1] - point1[1], 2));
    }
}