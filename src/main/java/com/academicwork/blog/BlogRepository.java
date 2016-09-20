package com.academicwork.blog;

import java.sql.SQLException;
import java.util.List;

public interface BlogRepository {
    List<Blog> listBlogs();
    Blog getBlog(long blogId);
    User getAuthorOf(Blog blog);
    List<BlogPost> getEntriesIn(Blog blog);
    List<Comment> getComments(long ID);
    void addComment(String a, String b, String c, long ID) throws SQLException;
}
