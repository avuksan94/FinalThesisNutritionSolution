package fin.av.thesis.REST.Mapper.ResponseMapper;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DTO.Response.IngredientResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientResponseMapper {
    @Mapping(source = "nutrients", target = "nutrients")
    IngredientResponseDTO IngredientToDTOIngredientRes(Ingredient ingredient);
    @Mapping(source = "nutrients", target = "nutrients")
    Ingredient DTOIngredientResToIngredient(IngredientResponseDTO dtoIngredient);
}
