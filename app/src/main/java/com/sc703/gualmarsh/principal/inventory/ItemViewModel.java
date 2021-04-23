package com.sc703.gualmarsh.principal.inventory;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ItemViewModel extends ViewModel {
    private final MutableLiveData<String> categoryCode = new MutableLiveData<>();
    private final MutableLiveData<String> categoryKey = new MutableLiveData<>();
    private final MutableLiveData<String> categoryQuantity = new MutableLiveData<>();
    private final MutableLiveData<String> productCode = new MutableLiveData<>();
    private final MutableLiveData<String> productName = new MutableLiveData<>();
    private final MutableLiveData<String> productQuantity = new MutableLiveData<>();
    private final MutableLiveData<String> productPrice = new MutableLiveData<>();
    private final MutableLiveData<String> productDescription = new MutableLiveData<>();
    private final MutableLiveData<String> productSearch = new MutableLiveData<>();
    private final MutableLiveData<String> productCount = new MutableLiveData<>();
    private final MutableLiveData<String> productKey = new MutableLiveData<>();
    private final MutableLiveData<String> productCategory = new MutableLiveData<>();
    private final MutableLiveData<String> productChanged = new MutableLiveData<>();

    public void setCategoryCode(String category){
        categoryCode.setValue(category);
    }
    public void setCategoryKey(String key){
        categoryKey.setValue(key);
    }
    public void setCategoryQuantity(String quantity){
        categoryQuantity.setValue(quantity);
    }
    public void setProductCode(String product){
        productCode.setValue(product);
    }
    public void setProductCount(String count){
        productCount.setValue(count);
    }
    public void setProductName(String name){
        productName.setValue(name);
    }
    public void setProductQuantity(String quantity){
        productQuantity.setValue(quantity);
    }
    public void setProductPrice(String price){
        productPrice.setValue(price);
    }
    public void setProductDescription(String description){
        productDescription.setValue(description);
    }
    public void setProductSearch(String search){
        productSearch.setValue(search);
    }
    public void setProductKey(String key){
        productKey.setValue(key);
    }
    public void setProductCategory(String category){
        productCategory.setValue(category);
    }
    public void setProductChanged(String changed){
        productChanged.setValue(changed);
    }
    public LiveData<String> getCategoryCode() {
        return categoryCode;
    }
    public MutableLiveData<String> getCategoryKey() {
        return categoryKey;
    }
    public MutableLiveData<String> getCategoryQuantity() {
        return categoryQuantity;
    }
    public LiveData<String> getProductCode() {
        return productCode;
    }
    public LiveData<String> getProductCount() {
        return productCount;
    }
    public MutableLiveData<String> getProductName() {
        return productName;
    }
    public MutableLiveData<String> getProductQuantity() {
        return productQuantity;
    }
    public MutableLiveData<String> getProductPrice() {
        return productPrice;
    }
    public MutableLiveData<String> getProductDescription() {
        return productDescription;
    }
    public MutableLiveData<String> getProductSearch() {
        return productSearch;
    }
    public MutableLiveData<String> getProductKey() {
        return productKey;
    }
    public MutableLiveData<String> getProductCategory() {
        return productCategory;
    }
    public MutableLiveData<String> getProductChanged() {
        return productChanged;
    }
}
