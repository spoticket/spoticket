package com.spoticket.game.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

public class GenericPagedModel<T> extends PagedModel<T> {

    public GenericPagedModel(Page<T> page) {
        super(new PageImpl<>(
                page.getContent(),
                page.getPageable(),
                page.getTotalElements()
                )
        );
    }

    public static <T> GenericPagedModel<T> of(Page<T> page) {
        return new GenericPagedModel<>(page);
    }

}
