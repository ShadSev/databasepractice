package com.academicwork.blog;

import java.time.LocalDateTime;

public class BlogPost {

    public final long ID;
    public final long Blog_ID;
    public final String Title;
    public final String Body;
    public final LocalDateTime Date;

    public BlogPost(long id, String title, String body, LocalDateTime Date, long blogId) {
        this.ID = id;
        this.Title = title;
        this.Body = body;
        this.Date = Date;
        this.Blog_ID = blogId;
    }
}
