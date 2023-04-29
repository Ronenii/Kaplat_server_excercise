package com.Ronenii.Kaplat_server_excercise.Model;

enum Status
{
    PENDING,
    LATE,
    DONE
}
public class TODO {
    private int id;
    private String Title, Content;
    private long DueDate;
    private Status Status;

    public String toString(){
        return "TODO(id=" + this.id +
                ", Title=" + this.Title +
                ", Content=" + this.Content +
                ", DueDate=" + this.DueDate +
                ", Status=" + this.Status + ")";}
}
