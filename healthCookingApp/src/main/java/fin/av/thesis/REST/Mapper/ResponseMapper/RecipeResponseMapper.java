package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DTO.Response.RecipeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeResponseMapper {
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritionSummary", target = "nutritionSummary")
    RecipeResponseDTO RecipeToDTORecipeRes(Recipe recipe);
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritionSummary", target = "nutritionSummary")
    Recipe DTORecipeResToRecipe(RecipeResponseDTO dtoRecipe);
}
