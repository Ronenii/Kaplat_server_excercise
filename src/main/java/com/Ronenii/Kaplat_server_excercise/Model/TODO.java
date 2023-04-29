package com.Ronenii.Kaplat_server_excercise.Model;

enum eStatus
{
    PENDING,
    LATE,
    DONE
}
public class TODO {
    static private int idCount = 1;
    private int id;
    private String Title, Content;
    private long dueDate;
    private eStatus Status;

    public TODO(String Title, String Content, long dueDate, String Status)
    {
        this.id = idCount++;
        this.Title = Title;
        this.Content = Content;
        this.dueDate = dueDate;
        this.Status = eStatus.valueOf(Status);
        System.out.print(this);
    }

    public String toString(){
        return "TODO(id=" + id +
                ", Title=" + this.Title +
                ", Content=" + this.Content +
                ", DueDate=" + this.dueDate +
                ", Status=" + this.Status + ")";}
}
