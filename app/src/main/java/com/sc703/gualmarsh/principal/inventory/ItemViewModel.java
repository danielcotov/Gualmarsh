package com.sc703.gualmarsh.principal.inventory;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ItemViewModel extends ViewModel {
    private final MutableLiveData<String> categoryCode = new MutableLiveData<>();
    private final MutableLiveData<String> productCode = new MutableLiveData<>();
    private final MutableLiveData<String> defaultView = new MutableLiveData<>();
    public void setCategoryCode(String category){
        categoryCode.setValue(category);
    }
    public void setProductCode(String product){
        productCode.setValue(product);
    }
    public void setDefaultView(String view){
        defaultView.setValue(view);
    }
    public LiveData<String> getCategoryCode() {
        return categoryCode;
    }
    public LiveData<String> getProductCode() {
        return productCode;
    }
    public LiveData<String> getDefaultView() {
        return defaultView;
    }
}
