package com.academicwork.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SqlServerBlogrepository implements BlogRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Blog> listBlogs() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM [Blog]")) {
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()) blogs.add(rsBlog(rs));
            return blogs;
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public Blog getBlog(long blogId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Id, Title FROM [Blog] WHERE Id = ?")) {
            ps.setLong(1, blogId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new BlogRepositoryException("No blog with ID " + blogId);
                else return rsBlog(rs);
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public User getAuthorOf(Blog blog) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [UserID], [Users].[UserName],[Users].[FirstName], [Users].[LastName], [Users].[Email] " +
                     "FROM [Users] JOIN [Blog] ON Blog.User_Id = Users.UserID " +
                     "WHERE blog.Id = ?")) {
            ps.setLong(1, blog.Id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new BlogRepositoryException("No blog with ID " + blog.Id);
                else return new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public List<BlogPost> getEntriesIn(Blog blog) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT BlogPosts.ID, BlogPosts.Title, BlogPosts.Body, BlogPosts.Date, BlogPosts.Blog_Id " +
                     "FROM [BlogPosts] WHERE BlogPosts.Blog_ID = ? ORDER BY BlogPosts.Date DESC")) {
            ps.setLong(1, blog.Id);
            try (ResultSet rs = ps.executeQuery()) {
                List<BlogPost> posts = new ArrayList<>();
                while (rs.next()) posts.add(rsPost(rs));
                return posts;
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    private BlogPost rsPost(ResultSet rs) throws SQLException {
        return new BlogPost(
                rs.getLong("Id"),
                rs.getString("Title"),
                rs.getString("Body"),
                rs.getTimestamp("Date").toLocalDateTime(),
                rs.getLong("Blog_Id")
        );
    }

    private Blog rsBlog(ResultSet rs) throws SQLException {
        return new Blog(rs.getLong("id"), rs.getString("title"));
    }

    private Comment rsComment(ResultSet rs) throws SQLException {
        return new Comment(rs.getString("Message"), rs.getString("Title"), rs.getString("Name"), rs.getLong("BlogPost_ID"));
    }

    @Override
    public List<Comment> getComments(long ID) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT Message, Title, Name, BlogPost_ID FROM [Comments] WHERE BlogPost_ID = ?")) {
            ps.setLong(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) comments.add(rsComment(rs));
                return comments;
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }


    @Override
    public void addComment(String Title, String Message, String Name, long ID) throws SQLException {
        String insertSQL = "INSERT INTO Comments (Title, Message, Name, BlogPost_ID)\n" +
                "VALUES(?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ps.setString(1, Title);
            ps.setString(2, Message);
            ps.setString(3, Name);
            ps.setLong(4, ID);
            ps.executeUpdate();
//            try (ps.executeUpdate()) {
//                if (!rs.next()) throw new BlogRepositoryException();
//            }
//        } catch (SQLException e) {
//            throw new BlogRepositoryException(e);
//        }
        }
    }

}
