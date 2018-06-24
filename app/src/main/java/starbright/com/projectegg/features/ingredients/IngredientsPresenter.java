package starbright.com.projectegg.features.ingredients;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import starbright.com.projectegg.data.AppRepository;
import starbright.com.projectegg.data.local.model.Ingredient;
import starbright.com.projectegg.util.scheduler.BaseSchedulerProvider;

class IngredientsPresenter implements IngredientsContract.Presenter {

    private final AppRepository mRepository;
    private final IngredientsContract.View mView;
    private final BaseSchedulerProvider mSchedulerProvider;
    private CompositeDisposable mCompositeDisposable;

    IngredientsPresenter(
            AppRepository repo,
            IngredientsContract.View view,
            BaseSchedulerProvider schedulerProvider) {
        mRepository = repo;
        mView = view;
        mView.setPresenter(this);
        mSchedulerProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void start() {
        mView.setupEtSearchIngredients();
        mView.setupRvIngredientSuggestion();
    }

    @Override
    public void handleActionButtonClicked(String query) {
        if (query.isEmpty()) {
            mView.openCamera();
        } else {
            mView.clearEtSearchQuery();
        }
    }

    @Override
    public void handleOnSuggestionTextChanged(String query) {
        mView.hideSearchSuggestion();
        if (query.isEmpty()) {
            mView.hideSuggestionProgressBar();
            mView.showActionCamera();
        } else {
            mView.hideActionButton();
            mView.showSuggestionProgressBar();
        }
    }

    @Override
    public void searchIngredient(String query) {
        if (query.isEmpty()) {
            return;
        }

        mCompositeDisposable.add(
                mRepository.searchIngredient(query)
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Ingredient>>() {
                    @Override
                    public void accept(List<Ingredient> ingredients) {
                        mView.hideSuggestionProgressBar();
                        if (ingredients.isEmpty()) {
                            mView.showItemEmptyToast();
                        }
                        mView.showActionClear();
                        mView.showSearchSuggestion(ingredients);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        mView.hideSuggestionProgressBar();
                        mView.showActionClear();
                    }
                })
        );
    }
}