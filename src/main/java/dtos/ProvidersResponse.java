package dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProvidersResponse {

    private List<Provider> providers;
    private Integer pages;

}
