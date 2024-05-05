package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.Recipe;
import fin.av.thesis.DTO.Request.RecipeRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeRequestMapper {
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritionSummary", target = "nutritionSummary")
    Recipe DTORecipeReqToRecipe(RecipeRequestDTO dtoRecipe);
    @Mapping(source = "ingredients", target = "ingredients")
    @Mapping(source = "nutritionSummary", target = "nutritionSummary")
    RecipeRequestDTO RecipeToDTORecipeReq(Recipe recipe);
}
