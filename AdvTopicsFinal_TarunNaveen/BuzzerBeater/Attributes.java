package BuzzerBeater;
import java.util.*;

public class Attributes {

    public static final Map<String, Integer> shooting = new TreeMap<>();
    public static final Map<String, Integer> midrange = new TreeMap<>();
    public static final Map<String, Integer> inside = new TreeMap<>();
    public static final Map<String, Integer> speed = new TreeMap<>();
    public static final Map<String, Integer> odefense = new TreeMap<>();
    public static final Map<String, Integer> idefense = new TreeMap<>();
    public static final Map<String, Integer> strength = new TreeMap<>();

    public Attributes() {
        addShooting();
        addMid();
        addInside();
        addSpd();
        addOD();
        addID();
        addStrength();
    }

    public void addShooting() {
        shooting.put("S. Curry", 100);
        shooting.put("L. James", 94);
        shooting.put("M. Jordan", 91);
        shooting.put("L. Doncic", 97);
        shooting.put("K. Bryant", 95);
        shooting.put("S. O'Neal", 25);
        shooting.put("G. Antetekounmpo", 40);
        shooting.put("D. Nowitzki", 96);
        shooting.put("N. Jokic", 88);
        shooting.put("K. Durant", 98);
    }

    public void addMid() {
        midrange.put("S. Curry", 95);
        midrange.put("L. James", 95);
        midrange.put("M. Jordan", 99);
        midrange.put("L. Doncic", 96);
        midrange.put("K. Bryant", 99);
        midrange.put("S. O'Neal", 25);
        midrange.put("G. Antetekounmpo", 60);
        midrange.put("D. Nowitzki", 99);
        midrange.put("N. Jokic", 89);
        midrange.put("K. Durant", 99);
    }

    public void addInside() {
        inside.put("S. Curry", 93);
        inside.put("L. James", 97);
        inside.put("M. Jordan", 99);
        inside.put("L. Doncic", 97);
        inside.put("K. Bryant", 96);
        inside.put("S. O'Neal", 100);
        inside.put("G. Antetekounmpo", 99);
        inside.put("D. Nowitzki", 89);
        inside.put("N. Jokic", 97);
        inside.put("K. Durant", 93);
    }

    public void addSpd() {
        speed.put("S. Curry", 97);
        speed.put("L. James", 90);
        speed.put("M. Jordan", 95);
        speed.put("L. Doncic", 81);
        speed.put("K. Bryant", 94);
        speed.put("S. O'Neal", 65);
        speed.put("G. Antetekounmpo", 85);
        speed.put("D. Nowitzki", 80);
        speed.put("N. Jokic", 67);
        speed.put("K. Durant", 92);
    }

    public void addOD() {
        odefense.put("S. Curry", 85);
        odefense.put("L. James", 85);
        odefense.put("M. Jordan", 99);
        odefense.put("L. Doncic", 85);
        odefense.put("K. Bryant", 97);
        odefense.put("S. O'Neal", 80);
        odefense.put("G. Antetekounmpo", 85);
        odefense.put("D. Nowitzki", 80);
        odefense.put("N. Jokic", 85);
        odefense.put("K. Durant", 92);
    }

    public void addID() {
        idefense.put("S. Curry", 75);
        idefense.put("L. James", 93);
        idefense.put("M. Jordan", 95);
        idefense.put("L. Doncic", 85);
        idefense.put("K. Bryant", 87);
        idefense.put("S. O'Neal", 99);
        idefense.put("G. Antetekounmpo", 99);
        idefense.put("D. Nowitzki", 80);
        idefense.put("N. Jokic", 90);
        idefense.put("K. Durant", 86);
    }

    public void addStrength() {
        strength.put("S. Curry", 75);
        strength.put("L. James", 93);
        strength.put("M. Jordan", 85);
        strength.put("L. Doncic", 75);
        strength.put("K. Bryant", 85);
        strength.put("S. O'Neal", 99);
        strength.put("G. Antetekounmpo", 99);
        strength.put("D. Nowitzki", 90);
        strength.put("N. Jokic", 90);
        strength.put("K. Durant", 75);
    }

    public static String getStringRep(String name) {
        return String.format("3P: %d   MID: %d   INSIDE: %d   SPD: %d   |IDEF: %d   ODEF: %d   STR: %d", shooting.get(name), midrange.get(name), inside.get(name),          
                    speed.get(name), idefense.get(name), odefense.get(name), strength.get(name));
    }

    public static Integer getName(int spdRtg) {
        Integer[] arr = (Integer[]) speed.values().toArray();
        for (int count = 0; count < arr.length; count++) {
            if (((Integer) spdRtg).equals(arr[count])) {
                return (Integer) count;
            }
        }
        return 0;
    }

}
