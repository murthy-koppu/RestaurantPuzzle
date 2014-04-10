package com.castlight.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class OrderItemList {
	
	private List<MenuItemRelations> itemPricesList;
	private double totalPrice;
	
	public List<MenuItemRelations> getItemPricesList() {
		return itemPricesList;
	}

	public void setItemPricesList(List<MenuItemRelations> itemPricesList) {
		this.itemPricesList = itemPricesList;
	}
	
	public OrderItemList(){
		itemPricesList = new ArrayList<MenuItemRelations>();
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	
}
