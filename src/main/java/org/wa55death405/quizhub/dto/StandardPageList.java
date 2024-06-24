package org.wa55death405.quizhub.dto;

import lombok.Data;

import java.util.List;

/*
    * this class is used as standard response for the paginated list
    * @param <T> the type of the items in the list
 */
@Data
public class StandardPageList<T>{
    private Integer currentPage;
    private Integer currentItemsSize;
//    TODO: change 'totalItems' to 'totalItemsSize'
    private Long totalItems;
    private List<T> items;
}
