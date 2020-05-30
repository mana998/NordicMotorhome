package com.example.demo.Model;

import java.util.List;

public class ItemCreation {
//wrapper class for retrieving list of items from the html form

    private List<Item> items;

    public ItemCreation(){}

    public ItemCreation(List<Item> items) {
        this.items = items;
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public void addDifference(List<Item> itemList){
        boolean add;
        //traverses through every item from the list
        for (Item item : itemList){
            //sets add to true - add it to the new list
            add = true;
            //traverses through list associated with agreement
            for (Item originalItem: items){
                //if list contains item with the same id, sets add to false
                //item already exists in the list so no need to add it
                if (originalItem.getId()==item.getId()){
                    add = false;
                }
            }
            //if add remains true, adds item to the list
            if (add){
                this.items.add(item);
            }
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
