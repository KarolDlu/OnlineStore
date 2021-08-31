package com.karold.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.karold.onlinestore.payload.ProductReviewRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JsonIgnoreProperties({"password", "addresses", "userType"})
    private User author;

    @ManyToOne
    private Product product;

    @NotBlank
    private String title;

    @Lob
    @NotBlank
    private String content;

    public ProductReview(@NotBlank User author, Product product, @NotBlank String title, @NotBlank String content) {
        this.author = author;
        this.product = product;
        this.title = title;
        this.content = content;
    }

    public void update(ProductReviewRequest updatedRequest){
        this.title = updatedRequest.getTitle();
        this.content = updatedRequest.getContent();
    }
}
