package com.march.bad_comments_filter.dto;

import java.util.List;

public record CommentResponse(int id, List<PredictionResponse> labelPrediction) {
}
