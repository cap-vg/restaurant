package service.impl;

import domain.DietType;
import domain.eto.Meal;
import domain.eto.Produce;
import service.api.MenuService;
import service.exception.NoFoodFoundException;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MenuServiceImpl implements MenuService {
    /**
     * For a given List of meals, find all meals that satisfy the predicate fn
     *
     * @param meals
     * @param fn
     * @return list of found meals
     */
    private List<Meal> getAllByPredicate(List<Meal> meals, Predicate<Meal> fn){
        return meals.stream().filter(fn).collect(Collectors.toList());
    }

    /**
     * For a given List of meals, find all meals that satisfy the predicate fn
     *
     * @param meals
     * @param getterNonNull
     * @param fn
     * @return list of found meals
     */
    private List<Meal> getAllByPredicateWithTypeChecking(List<Meal> meals, Predicate<Meal> getterNonNull ,  Predicate<Meal> fn){
        if (meals == null) {
            throw new NoFoodFoundException();
        }
         List<Meal> res = meals.stream()
                .filter(Objects::nonNull)
                .filter(getterNonNull)
                .filter(fn)
                .collect(Collectors.toList());
         // Throwing errors when list is empty is an antipattern
         if (res.isEmpty()) {
             throw new NoFoodFoundException();
         }
         return res;
    }


    /**
     * For a given List of meals, find all meals that are of dietType = {@link DietType#VEGETARIAN}.
     * Hint: You can copy the implementation from MenuExample
     *
     * @param meals
     * @return list of found meals
     */
    @Override
    public List<Meal>findVegetarianFood (List<Meal> meals) {
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> meal.getDietType() != null,
                meal -> meal.getDietType().isEquivalent(DietType.VEGETARIAN));
    }

    /**
     * For a given List of meals, find all meals that are of certain dietType
     *
     * @param meals
     * @param diet
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodByType(List<Meal> meals, DietType diet) {
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> meal.getDietType() != null,
                meal -> meal.getDietType().isEquivalent(diet));
    }

    /**
     * For a given List of meals return meals that cost less than given parameter.
     *
     * @param meals
     * @param price
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodStartingWithName(List<Meal> meals, Integer price) {
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> true,
                meal -> meal.getPrice() < price);
    }

    /**
     * For a given List of meals return meals that have calorie intake between two values
     *
     * @param meals
     * @param minCalories
     * @param maxCalories
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodCheaperWithCalories(List<Meal> meals, Integer minCalories, Integer maxCalories) {
        return this.getAllByPredicateWithTypeChecking( meals,
                meal -> true,
                meal -> minCalories < meal.getCalories() && meal.getCalories() < maxCalories);
    }

    /**
     * For a given List of meals return meals that name starts with a String. This method ignores character case when
     * searching.
     *
     * @param meals
     * @param name  - can be a partial String
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodStartingWithName(List<Meal> meals, String name) {
        // String.startsWith, is case-sensitive and using toLowerCase is wrong
        // See: https://stackoverflow.com/a/15518878/14731
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> true,
                meal -> meal.getName().regionMatches(true, 0, name,0, name.length()));
    }

    /**
     * For a given List of meals return meals that does contain certain product
     *
     * @param meals
     * @param product product that is in the meal
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodContaining(List<Meal> meals, Produce product) {
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> meal.getProducts() != null,
                meal -> meal.getProducts().contains(product));
    }

    /**
     * For a given List of meals return meals that does not contain any of the products
     *
     * @param meals
     * @param products that should be excluded from the menu
     * @return list of found meals
     */
    @Override
    public List<Meal> findFoodExcludingAll(List<Meal> meals, List<Produce> products) {
        // could use Collections.disjoint
        return this.getAllByPredicateWithTypeChecking(meals,
                meal -> meal.getProducts() != null,
                meal -> meal.getProducts().stream().noneMatch(products::contains));
    }
}
