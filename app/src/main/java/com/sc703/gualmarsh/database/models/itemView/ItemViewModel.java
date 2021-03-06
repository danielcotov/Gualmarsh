package com.sc703.gualmarsh.database.models.itemView;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ItemViewModel extends ViewModel {
    private final MutableLiveData<String> categoryCode = new MutableLiveData<>();
    private final MutableLiveData<String> productCode = new MutableLiveData<>();
    private final MutableLiveData<String> productName = new MutableLiveData<>();
    private final MutableLiveData<String> productQuantity = new MutableLiveData<>();
    private final MutableLiveData<String> productPrice = new MutableLiveData<>();
    private final MutableLiveData<String> productDescription = new MutableLiveData<>();
    private final MutableLiveData<String> productExpiration = new MutableLiveData<>();
    private final MutableLiveData<String> productSearch = new MutableLiveData<>();
    private final MutableLiveData<String> productCount = new MutableLiveData<>();
    private final MutableLiveData<String> productKey = new MutableLiveData<>();
    private final MutableLiveData<String> productCategory = new MutableLiveData<>();
    private final MutableLiveData<String> productChanged = new MutableLiveData<>();
    private final MutableLiveData<String> lastUpdated = new MutableLiveData<>();
    private final MutableLiveData<String> imagePath = new MutableLiveData<>();

    public void setCategoryCode(String category){
        categoryCode.setValue(category);
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
    public void setProductExpiration(String expiration){
        productExpiration.setValue(expiration);
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
    public void setLastUpdated(String date){
        lastUpdated.setValue(date);
    }
    public void setImagePath(String path){
        imagePath.setValue(path);
    }
    public LiveData<String> getCategoryCode() {
        return categoryCode;
    }
    public LiveData<String> getProductCode() {
        return productCode;
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
    public MutableLiveData<String> getProductExpiration() {
        return productExpiration;
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
    public MutableLiveData<String> getLastUpdated() {
        return lastUpdated;
    }
    public MutableLiveData<String> getImagePath() {
        return imagePath;
    }
}
