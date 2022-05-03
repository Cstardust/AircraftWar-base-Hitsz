package edu.hitsz.user;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable ,Comparable<User>{
    public User(String n,int s){
        name = n;
        score = s;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strDate = df.format(new Date());
    }

    public User(String n,int s,String date){
        name = n;
        score = s;
        strDate = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrDate() {
        return strDate;
    }

    @Override
    public String toString()
    {
        return "User: "+getName()+" , "+"score: "+getScore() + " , " + strDate;
    }
    @Override
    public int compareTo(User user)
    {
        //  从大到小排序
        return user.getScore() - this.getScore();
    }

    @Override
    public boolean equals(Object o)
    {
        if(o==this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User u = (User) o;
        return this.getStrDate().equals(u.getStrDate()) && this.getScore()==u.getScore() && this.getName().equals(u.getName());
    }

    private int score;
    private String name;
    private String strDate;
}
