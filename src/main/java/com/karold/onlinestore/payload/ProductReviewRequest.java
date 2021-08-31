package com.karold.onlinestore.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductReviewRequest {

    private Long productId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
