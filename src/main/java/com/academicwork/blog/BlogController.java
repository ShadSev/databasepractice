package com.academicwork.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Controller
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/blog/")
    public ModelAndView listBlogs() {
        return new ModelAndView("blog/list")
                .addObject("blogs", blogRepository.listBlogs());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/blog/{blogId}/")
    public ModelAndView listPosts(@PathVariable long blogId) {
        Blog blog = blogRepository.getBlog(blogId);
        return new ModelAndView("blog/posts")
                .addObject("blog", blog)
                .addObject("author", blogRepository.getAuthorOf(blog))
                .addObject("posts", blogRepository.getEntriesIn(blog));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/blog/{blogId}/posts/{ID}")
    public ModelAndView writeComments(@PathVariable long ID) {
        return new ModelAndView("blog/comments").addObject("comments", blogRepository.getComments(ID)).addObject("ID", ID);
    }

    @RequestMapping(path = "/låtsas/", method = RequestMethod.POST)
    public ModelAndView addComment(@RequestParam long BlogPost_ID, @RequestParam String message, @RequestParam String title, @RequestParam String name) throws SQLException {
        blogRepository.addComment(title, message, name, BlogPost_ID);
        return new ModelAndView("blog/comments").addObject("comments", blogRepository.getComments(BlogPost_ID)).addObject("ID", BlogPost_ID);
    }

}
