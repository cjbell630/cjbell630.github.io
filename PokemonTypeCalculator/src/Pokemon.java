public class Pokemon {
    private String name;
    private FinalType type;
    public Pokemon(String pokemonName, FinalType pokemonFinalType) {
        name = pokemonName;
        type = pokemonFinalType;
    }

    public FinalType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " (" + type.toString() + ")";
    }
}
