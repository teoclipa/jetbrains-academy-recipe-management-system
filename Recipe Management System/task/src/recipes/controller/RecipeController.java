package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDto;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.RecipeRepository;
import recipes.repository.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findByEmail(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/new")
    public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe recipe) {
        recipe.setUser(getCurrentUser());
        recipe.setDate(LocalDateTime.now());
        Recipe savedRecipe = recipeRepository.save(recipe);
        return ResponseEntity.ok().body(Map.of("id", savedRecipe.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> new RecipeDto(recipe.getName(), recipe.getCategory(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections(), recipe.getDate()))
                .map(recipeDto -> new ResponseEntity<>(recipeDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe newRecipe) {
        User currentUser = getCurrentUser();
        return recipeRepository.findById(id)
                .map(recipe -> {
                    if (!recipe.getUser().equals(currentUser)) {
                        return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
                    }
                    recipe.setName(newRecipe.getName());
                    recipe.setCategory(newRecipe.getCategory());
                    recipe.setDescription(newRecipe.getDescription());
                    recipe.setIngredients(newRecipe.getIngredients());
                    recipe.setDirections(newRecipe.getDirections());
                    recipe.setDate(LocalDateTime.now());
                    recipeRepository.save(recipe);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent() && optionalRecipe.get().getUser().equals(currentUser)) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else if (optionalRecipe.isPresent() && !optionalRecipe.get().getUser().equals(currentUser)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
