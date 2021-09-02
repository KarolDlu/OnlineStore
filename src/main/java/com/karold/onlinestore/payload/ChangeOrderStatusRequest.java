package com.karold.onlinestore.payload;

import com.karold.onlinestore.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
public class ChangeOrderStatusRequest {

    @NotBlank
    private UUID orderNumber;

    private OrderStatus status;
}
