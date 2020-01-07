package entities;

public class Region {

    private int id;
    private String abb;
    private String name;

    public Region(int id, String abb, String name){
        this.id = id;
        this.abb = abb;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " " + abb + " " + name;
    }
}
