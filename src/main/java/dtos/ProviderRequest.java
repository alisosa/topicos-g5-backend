package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProviderRequest {

    private Provider provider;
    private List<Question> questions;
}
