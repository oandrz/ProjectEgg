/**
 * Created by Andreas on 22/9/2018.
 */

package starbright.com.projectegg.features.recipelist

import io.reactivex.disposables.CompositeDisposable
import starbright.com.projectegg.R
import starbright.com.projectegg.data.AppRepository
import starbright.com.projectegg.data.model.Ingredient
import starbright.com.projectegg.data.model.Recipe
import starbright.com.projectegg.features.base.BasePresenter
import starbright.com.projectegg.util.NetworkHelper
import starbright.com.projectegg.util.scheduler.SchedulerProviderContract
import javax.inject.Inject

class RecipeListPresenter @Inject constructor(
    schedulerProvider: SchedulerProviderContract,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val mRepository: AppRepository
) : BasePresenter<RecipeListContract.View>(schedulerProvider, compositeDisposable, networkHelper), RecipeListContract.Presenter {

    private var recipes: List<Recipe> = listOf()
    private var ingredients: List<Ingredient> = listOf()

    override fun onCreateScreen() {
        view.let {
            val ingredients = it.provideIngredients()
            if (ingredients != null) {
                this.ingredients = ingredients
            }
            it.setupView()
        }
        getRecipesBasedIngredients(mapIngredients())
    }

    override fun handleListItemClicked(position: Int) {
        view.showDetail(recipes[position].id.toString())
    }

    override fun handleRefresh() {
        getRecipesBasedIngredients(mapIngredients())
    }

    override fun setIngredients(ingredients: MutableList<Ingredient>) {
        this.ingredients = ingredients
    }

    private fun getRecipesBasedIngredients(ingredients: String) {
        if (!isConnectedToInternet()) view.showError(R.string.server_connection_error)
        view.showLoadingBar()
        compositeDisposable.add(
            mRepository.getRecipes(ingredients, 0)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ recipes ->
                    this.recipes = recipes.toMutableList()
                    view.hideLoadingBar()
                    view.bindRecipesToList(recipes.toMutableList())
                }, { throwable ->
                    view.hideLoadingBar()
                    view.showErrorSnackBar(throwable.message ?: "")
                })
        )
    }

    private fun mapIngredients(): String {
        val stringBuilder = StringBuilder()
        var ingredientSize = ingredients.size
        for (ingredient in ingredients) {
            stringBuilder.append(ingredient.name)
            ingredientSize--
            if (ingredientSize > 0) {
                stringBuilder.append(", ")
            }
        }
        return stringBuilder.toString()
    }
}
