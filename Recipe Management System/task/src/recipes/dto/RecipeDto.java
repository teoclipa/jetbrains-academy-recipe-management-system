package recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    private String name;
    private String category;
    private String description;
    private List<String> ingredients;
    private List<String> directions;
    private LocalDateTime date;

}
