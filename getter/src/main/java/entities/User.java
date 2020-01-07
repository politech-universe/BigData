package entities;

public class User {
    private String id;
    private String name;
    private int subs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubs() {
        return subs;
    }

    public void setSubs(int subs) {
        this.subs = subs;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + subs;
    }
}
