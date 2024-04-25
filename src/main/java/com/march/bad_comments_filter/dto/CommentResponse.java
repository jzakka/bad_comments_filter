package com.march.bad_comments_filter.dto;

import java.util.Map;

public record CommentResponse(String id, Map<String, Double> labelPrediction) {
}
