/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproject_mari_kanepaul;

import java.util.Date;
/**
 *
 * @author Kane
 */
public class Loans {
    
    private int loan_id;
    private int copy_id;
    private int user_id;
    private Date issue_date;
    private Date due_date;
    private Date return_date;

    public Loans() {
    }

    public Loans(int loan_id, int copy_id, int user_id, Date issue_date, Date due_date, Date return_date) {
        this.loan_id = loan_id;
        this.copy_id = copy_id;
        this.user_id = user_id;
        this.issue_date = issue_date;
        this.due_date = due_date;
        this.return_date = return_date;
    }

    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getCopy_id() {
        return copy_id;
    }

    public void setCopy_id(int copy_id) {
        this.copy_id = copy_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }
    
    
    
}
