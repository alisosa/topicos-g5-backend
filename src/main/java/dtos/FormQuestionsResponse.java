package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FormQuestionsResponse {

    private List<Question> questions;
    private Provider provider;

}
