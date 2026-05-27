/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;

/**
 *
 * @author Kane
 */


public class Copies {
    
    private int copy_id;
    private String isbn;
    private int shelf_id;
    private BookStatus status; 

    public Copies() {
    }

    public Copies(int copy_id, String isbn, int shelf_id, BookStatus status) {
        this.copy_id = copy_id;
        this.isbn = isbn;
        this.shelf_id = shelf_id;
        this.status = status;
    }

    public int getCopy_id() {
        return copy_id;
    }

    public void setCopy_id(int copy_id) {
        this.copy_id = copy_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getShelf_id() {
        return shelf_id;
    }

    public void setShelf_id(int shelf_id) {
        this.shelf_id = shelf_id;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
}
