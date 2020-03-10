import java.util.Arrays;

enum PokemonType {
    NORMAL(0), FIRE(1), WATER(2), ELECTRIC(3), GRASS(4), ICE(5), FIGHTING(6), POISON(7),
    GROUND(8), FLYING(9), PSYCHIC(10), BUG(11), ROCK(12), GHOST(13), DRAGON(14), DARK(15),
    STEEL(16), FAIRY(17);
    public static final double[][] GEN_8_TYPE_CHART = new double[][]{
            /*Attacking*/                             /*Defending*/
            /*      N, Fir,   W,   E, Gra,   I, Fgt, Poi, Gro, Fly, Psy,   B,   R, Gho, Dra, Dar,   S, Fai*/
            /* N */{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 0, 1, 1, 0.5, 1},
            /*Fir*/{1, 0.5, 0.5, 1, 2, 2, 1, 1, 1, 1, 1, 2, 0.5, 1, 0.5, 1, 2, 1},
            /* W */{1, 2, 0.5, 1, 0.5, 1, 1, 1, 2, 1, 1, 1, 2, 1, 0.5, 1, 1, 1},
            /* E */{1, 1, 2, 0.5, 0.5, 1, 1, 1, 0, 2, 1, 1, 1, 1, 0.5, 1, 1, 1},
            /*Gra*/{1, 0.5, 2, 1, 0.5, 1, 1, 0.5, 2, 0.5, 1, 0.5, 2, 1, 0.5, 1, 0.5, 1},
            /* I */{1, 0.5, 0.5, 1, 2, 0.5, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 0.5, 1},
            /*Fgt*/{2, 1, 1, 1, 1, 2, 1, 0.5, 1, 0.5, 0.5, 0.5, 2, 0, 1, 2, 2, 0.5},
            /*Poi*/{1, 1, 1, 1, 2, 1, 1, 0.5, 0.5, 1, 1, 1, 0.5, 0.5, 1, 1, 0, 2},
            /*Gro*/{1, 2, 1, 2, 0.5, 1, 1, 2, 1, 0, 1, 0.5, 2, 1, 1, 1, 2, 0},
            /*Fly*/{1, 1, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 2, 0.5, 1, 1, 1, 0.5, 1},
            /*Psy*/{1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0.5, 1, 1, 1, 1, 0, 0.5, 1},
            /* B */{1, 0.5, 1, 1, 2, 1, 0.5, 0.5, 1, 0.5, 2, 1, 1, 0.5, 1, 2, 0.5, 0.5},
            /* R */{1, 2, 1, 1, 1, 2, 0.5, 1, 0.5, 2, 1, 2, 1, 1, 1, 1, 0.5, 1},
            /*Gho*/{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 1, 1},
            /*Dra*/{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0.5, 0},
            /*Dar*/{1, 1, 1, 1, 1, 1, 0.5, 1, 1, 1, 2, 1, 1, 2, 1, 0.5, 1, 0.5},
            /*Ste*/{1, 0.5, 0.5, 0.5, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 0.5, 2},
            /*Fai*/{1, 0.5, 1, 1, 1, 1, 2, 0.5, 1, 1, 1, 1, 1, 1, 2, 2, 0.5, 1}
    };
    int v;

    PokemonType(int id) {
        this.v = id;
    }

    public static PokemonType byID(int id) {
        return values()[id];
    }

    public static boolean contains(String string) {
        return Arrays.toString(values()).contains(string);
    }

    int asInt() {
        return this.v;
    }

    double[] getResistance(int gen) {
        assert gen < 9 && gen > 0 : "Generation " + gen + " does not exist.";
        assert gen > 5 : "Data not available yet for generation " + gen;
        double[][] completeChart = /*ternary for each chart*/ GEN_8_TYPE_CHART;
        int size = completeChart.length;
        double[] chart = new double[size];
        for (int i = 0; i < size; i++) {
            chart[i] = completeChart[i][this.v];
        }
        return chart;
    }

    double[] getMoveEffectiveness(int gen) {
        assert gen < 9 && gen > 0 : "Generation " + gen + " does not exist.";
        assert gen > 5 : "Data not available yet for generation " + gen;
        return /*ternary for each chart*/ GEN_8_TYPE_CHART[this.v];
    }
}

public class FinalType implements Comparable {
    private int gen;
    private double[] resistance;
    private double[] abilityRes;
    private int rank;
    private PokemonType[] types;
    private boolean hasAbility;

    public FinalType(int generation, PokemonType... allTypes) {
        assert allTypes.length > 0 && allTypes.length < 3 : "Amount of types must be 1 or 2. (Given: " + allTypes.length + ")";
        types = allTypes;
        gen = generation;
        hasAbility = false;
        if (types.length == 2) {
            assert types[0] != types[1] : "Types cannot be the same. (Type: " + types[0] + ")";
            resistance = combineResistance(types[0], types[1]);
        } else {
            resistance = types[0].getResistance(gen);
        }
        rank = calculateRank(resistance);
    }

    public FinalType(int generation, double[] abilityResistance, PokemonType... allTypes) {
        assert allTypes.length > 0 && allTypes.length < 3 : "Amount of types must be 1 or 2. (Given: " + allTypes.length + ")";
        types = allTypes;
        gen = generation;
        abilityRes = abilityResistance;
        hasAbility = true;
        if (types.length == 2) {
            assert types[0] != types[1] : "Types cannot be the same. (Type: " + types[0] + ")";
            resistance = combineResistance(types[0], types[1]);
        } else {
            resistance = types[0].getResistance(gen);
        }
        resistance = combineResistance(resistance, abilityResistance);
        rank = calculateRank(resistance);
    }

    public double[] getResistance() {
        return resistance;
    }

    public int getRank() {
        return rank;
    }

    public double[] combineResistance(double[] typeChartOne, double[] typeChartTwo) {
        assert typeChartOne.length == typeChartTwo.length : "Types cannot be combined, not the same length.";
        int size = typeChartOne.length;
        double[] combinedChart = new double[size];
        for (int i = 0; i < size; i++) {
            combinedChart[i] = typeChartOne[i] * typeChartTwo[i];
        }
        return combinedChart;
    }

    public double[] combineResistance(PokemonType typeOne, PokemonType typeTwo) {
        return combineResistance(typeOne.getResistance(gen), typeTwo.getResistance(gen));
    }

    public int calculateRank(double[] resistanceChart) {
        //double[] valid = new double[]{0, 0.25, 0.5, 1, 2, 4};
        int r = 0;
        for (double number : resistanceChart) {
            //assert Arrays.stream(valid).anyMatch(item -> item == number) : "Invalid double in chart: " + number;
            r += number == 0 ? 3 : number == 0.25 ? 2 : number == 0.5 ? 1 : number == 1 ? 0 : number == 2 ? -1 : -2;
        }
        return r;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        for (PokemonType type : types) {
            string.append(type).append(" ");
        }
        if(hasAbility){

            string.append("(Ability Bonus: Nullifies");
            for(int i=0; i<abilityRes.length; i++){
                if(abilityRes[i]==0){
                    string.append(" " + PokemonType.byID(i));
                }
            }
            string.append(")");
        }
        return string.toString().trim();
    }

    @Override
    public int compareTo(Object o) {

        if (this.getRank() > ((FinalType) o).getRank())
            return 1;
        else if (this.getRank() < ((FinalType) o).getRank())
            return -1;
        return 0;
    }
}
