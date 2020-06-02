package com.max.service.impl;

import com.max.dao.CommentRepository;
import com.max.po.Comment;
import com.max.service.CommentService;
import com.max.util.CommentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return CommentUtil.eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        //回复: 如果有父级,需要将父级set进来,之后在保存
        Long parentCommentId = comment.getParentComment().getId();
        System.out.println(parentCommentId);
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findOne(parentCommentId));
        } else {
            //发布评论:
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

}
