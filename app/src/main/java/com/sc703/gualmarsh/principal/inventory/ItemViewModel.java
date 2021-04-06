package com.sc703.gualmarsh.principal.inventory;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ItemViewModel extends ViewModel {
    private final MutableLiveData<String> itemCode = new MutableLiveData<>();
    private final MutableLiveData<String> itemViewType = new MutableLiveData<>();

    public void setCode(String category){
        itemCode.setValue(category);
    }
    public void setViewType(String viewType){
        itemViewType.setValue(viewType);
    }

    public LiveData<String> getCode() {
        return itemCode;
    }
    public LiveData<String> getViewType() {
        return itemViewType;
    }

}
