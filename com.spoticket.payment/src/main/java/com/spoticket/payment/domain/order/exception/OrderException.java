package com.spoticket.payment.domain.order.exception;

import com.spoticket.payment.domain.common.BaseException;
import lombok.Getter;

@Getter
public class OrderException extends BaseException {

    public OrderException(OrderErrorCode errorCode) {
        super(errorCode);
    }

}
