package model;

/**
 * Created by 140179 on 2015-06-12.
 */
public class Author {
    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
