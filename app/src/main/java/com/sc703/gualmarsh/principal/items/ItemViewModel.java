package com.sc703.gualmarsh.principal.items;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class ItemViewModel extends ViewModel {
    private final MutableLiveData<String> itemCode = new MutableLiveData<>();

    public void setCode(String category){
        itemCode.setValue(category);
    }

    public LiveData<String> getCode() {
        return itemCode;
    }

}
