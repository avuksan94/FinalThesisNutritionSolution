package fin.av.thesis.DAL.Repository;

import fin.av.thesis.DAL.Document.Nutrition.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends ReactiveMongoRepository<Ingredient, String> {
}
