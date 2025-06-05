package com.comicsdb.comics.comments;

import com.comicsdb.comics.auth.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    
    @GetMapping("/{volumeId}")
    public List<Comment> getComments(@PathVariable Long volumeId) {
        return commentRepository.findByVolumeIdOrderByTimestampDesc(volumeId);
    }
    
    @PostMapping
    public Comment addComment(
            @RequestBody CommentDto req,
            @AuthenticationPrincipal AppUser user
    ) {
        Comment comment = new Comment();
        comment.setVolumeId(req.getVolumeId());
        comment.setText(req.getText());
        comment.setUsername(user.getUsername());
        comment.setTimestamp(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal AppUser user) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) return ResponseEntity.notFound().build();
        
        if (!comment.get().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok().build();
    }
    
}
