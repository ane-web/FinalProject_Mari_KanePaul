/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;

/**
 *
 * @author Kane
 */
public class Shelves {
    
    private int shelf_id;
    private int floor;
    private String section;
    private String shelf_code;

    public Shelves() {
    }

    public Shelves(int shelf_id, int floor, String section, String shelf_code) {
        this.shelf_id = shelf_id;
        this.floor = floor;
        this.section = section;
        this.shelf_code = shelf_code;
    }

    public int getShelf_id() {
        return shelf_id;
    }

    public void setShelf_id(int shelf_id) {
        this.shelf_id = shelf_id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getShelf_code() {
        return shelf_code;
    }

    public void setShelf_code(String shelf_code) {
        this.shelf_code = shelf_code;
    }
    
    
    
    
}
