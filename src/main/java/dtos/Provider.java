package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Provider {

    private String name;
    private String rut;
    private Double score;
    private String logo;
    private String address;
    private String email;
    private String category;

    public Provider (String name, String rut, String logo){
        this.name = name;
        this.rut = rut;
        this.logo = logo;
    }

    public Provider (){}


}
