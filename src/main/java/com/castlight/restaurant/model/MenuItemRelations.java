package com.castlight.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRelations {
	
	private double price;
	private List<Integer> orderItemsIndexes;
	
	public MenuItemRelations(){
		setOrderItemsIndexes(new ArrayList<Integer>());
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public List<Integer> getOrderItemsIndexes() {
		return orderItemsIndexes;
	}

	public void setOrderItemsIndexes(List<Integer> orderedItemsIndexes) {
		this.orderItemsIndexes = orderedItemsIndexes;
	}
	
	public void addOrderItemIndex(int index){
		orderItemsIndexes.add(index);
	}
	

}
