package com.algafoods.api.model.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Getter
@Setter
public class PagedResponseModel<T> extends RepresentationModel<PagedResponseModel<T>> {

    private List<T> content;
    private PageModel page;

    public PagedResponseModel(List<T> content, PageModel page) {
        this.content = content;
        this.page = page;
    }

}