package com.academicwork.blog;

/**
 * Created by Administrator on 2016-09-19.
 */
public class Comment {
    public String Name;
    public String Message;
    public String Title;
    public long BlogPost_ID;

    public Comment(String message, String title, String name, long BlogPost_ID) {
        this.Title = title;
        this.Name = name;
        this.Message = message;
        this.BlogPost_ID = BlogPost_ID;
    }
}
