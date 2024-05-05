package fin.av.thesis.REST.Mapper.RequestMapper;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IngredientRequestMapper {
    @Mapping(source = "nutrients", target = "nutrients")
    Ingredient DTOIngredientReqToIngredient(IngredientRequestDTO dtoIngredient);
    @Mapping(source = "nutrients", target = "nutrients")
    IngredientRequestDTO IngredientToDTOIngredientReq(Ingredient ingredient);
}
