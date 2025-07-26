package com.example.myplaceapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myplaceapp.MyPlace

class MyPlacesViewModel: ViewModel() {
    var myPlaces = mutableListOf<MyPlace>()
        private set
    var selectPlaces = mutableListOf<MyPlace>()
        private set
    fun addPlace(place: MyPlace) {
        myPlaces.add(place)
    }

    fun toggleSelected(place:MyPlace){
        if(selectPlaces.contains(place)){
            selectPlaces.remove(place)
        }else{
            if(selectPlaces.size<2){
                selectPlaces.add(place)


            } else{
                selectPlaces.clear()
                selectPlaces.add(place)
            }
        }

    }
}