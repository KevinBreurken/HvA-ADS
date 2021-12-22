package graphs;

public class Country implements Identifiable {

    private String name;

    public Country(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Country country = (Country) o;
        return name.equals(country.name);
    }
}
