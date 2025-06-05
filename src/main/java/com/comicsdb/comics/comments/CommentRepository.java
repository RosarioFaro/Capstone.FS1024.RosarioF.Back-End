package com.comicsdb.comics.comments;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVolumeIdOrderByTimestampDesc(Long volumeId);
}