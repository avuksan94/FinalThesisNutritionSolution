package fin.av.thesis.REST.Mapper.Basic;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import fin.av.thesis.DAL.Document.Nutrition.Nutrients;
import fin.av.thesis.DTO.Request.IngredientRequestDTO;
import fin.av.thesis.DTO.Shared.NutrientsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NutrientsMapper {
    Nutrients DTONutrientsToNutrients(NutrientsDTO dtoNutrients);
    NutrientsDTO NutrientsToDTONutrients(Nutrients nutrients);
}
