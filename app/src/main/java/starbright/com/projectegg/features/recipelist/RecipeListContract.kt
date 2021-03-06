/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 9 - 8 - 2020.
 */

/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import starbright.com.projectegg.data.RecipeConfig
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.data.model.SortOption
import starbright.com.projectegg.enum.RecipeSortCategory
import starbright.com.projectegg.features.base.BasePresenterContract
import starbright.com.projectegg.features.base.BaseViewContract

class RecipeListContract {

    interface View : BaseViewContract {
        fun setupView()
        fun provideSearchConfig(): RecipeConfig
        fun showFooterLoading()
        fun hideFooterLoading()
        fun appendRecipes(recipes: List<Recipe>)
        fun showDetail(recipeId: String)
        fun showFilterBottomSheet(
            cuisines: List<String>,
            selectedCuisine: String?
        )

        fun showSortBottomSheet(
            sortOption: ArrayList<SortOption>,
            selectedSortOption: String
        )

        fun clearRecipe()
        fun hideFilterButton()
        fun showFilterButton()
        fun showResultEmptyState()
        fun showErrorState()
        fun disableLoadMore()
    }

    interface Presenter : BasePresenterContract {
        fun handleListItemClicked(selectedRecipeId: String)
        fun handleRefresh()
        fun handleLoadMore(lastPosition: Int)
        fun handleSortActionClicked()
        fun handleSortItemSelected(sortType: RecipeSortCategory)
        fun handleFilterActionClicked()
        fun handleFilterItemSelected(cuisine: String)
    }
}
