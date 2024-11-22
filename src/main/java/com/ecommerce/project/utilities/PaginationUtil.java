package com.ecommerce.project.utilities;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.payload.common.PaginatedResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationUtil {


    public static <E, D> PaginatedResponse<D> getPaginatedResponse(Page<E> entityPage, Class<D> dtoClass, ModelMapper modelMapper) {
        PaginatedResponse<D> response = new PaginatedResponse<>();

        // Convert entity list to DTO list using ModelMapper
        if (entityPage.isEmpty()) {
            throw new APIException("No items available.");
        }
        List<D> dtoList = entityPage.getContent().stream()
                .map(entity -> modelMapper.map(entity, dtoClass)).toList();
        response.setContent(dtoList);
        response.setPageNumber(entityPage.getNumber());
        response.setTotalElements(entityPage.getTotalElements());
        response.setPageSize(entityPage.getSize());
        response.setTotalPages(entityPage.getTotalPages());
        response.setLastPage(entityPage.isLast());

        return response;
    }

    //  Sort sortByAndOrder =
    //                sortOrder.equalsIgnoreCase("asc")
    //                        ? Sort.by(sortBy).ascending()
    //                        : Sort.by(sortBy).descending();

    public static Sort getSortByAndOrder(String sortBy, String sortOrder) {
        return sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}
