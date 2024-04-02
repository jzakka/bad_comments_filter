package com.march.bad_comments_filter.dto;

import java.util.List;

public record CommentResponse(String id, List<String> tags) {
}
