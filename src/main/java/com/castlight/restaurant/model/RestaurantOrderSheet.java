package com.castlight.restaurant.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.plaf.ListUI;

import com.castlight.restaurant.impl.OrderPriceOptimizer;

/***
 * 
 * @author murthykoppu
 * Restaurant Order Sheet saves number of times an item is repeated in menu and useful menu lines in the restaurant Order sheet 
 * orderItemCountInMenu[] saves number of times an item is repeated across different Meal products of Restaurant
 * bufferedItemPriceLists[] saves MenuItemLines(repeated across various meals) per each Item 
 * 
 * Each Item placed as an Order is given an index, this index is used to refer to items placed as order.
 */

public class RestaurantOrderSheet {
	
	private int[] orderItemCountInMenu;	
	private OrderItemList[] bufferedItemPriceLists;
	
	/***
	 * 
	 * @param noOfOrderedItems Number of Items requested by user as part of order
	 */
	
	public RestaurantOrderSheet(int noOfOrderedItems){
		orderItemCountInMenu = new int[noOfOrderedItems];
		bufferedItemPriceLists = new OrderItemList[noOfOrderedItems];
		for(int k =0; k < noOfOrderedItems; k++){
			bufferedItemPriceLists[k] = new OrderItemList();
		}
	}
	

	public int[] getOrderItemCountInMenu() {
		return orderItemCountInMenu;
	}

	public void setOrderItemCountInMenu(int[] orderItemCountInMenu) {
		this.orderItemCountInMenu = orderItemCountInMenu;
	}

	public boolean isAllItemsAvail(){
		for(int itemCount : getOrderItemCountInMenu()){
			if(itemCount < 1){
				return false;
			}
		}
		return true;
	}	

	/***
	 * Adds Menu Item to Buffer List.
	 * @param menuItemPrice
	 */
	private void addMenuItemToBufferedList(MenuItemRelations menuItemPrice){
		for(Integer orderItemIndex: menuItemPrice.getOrderItemsIndexes()){
			bufferedItemPriceLists[orderItemIndex].getItemPricesList().add(menuItemPrice);
			orderItemCountInMenu[orderItemIndex]++;
		}
	}

	/***
	 * Validates whether menuItem is useful or redundant in adding to bufferedList()
	 * Also removes redundant menuItems if bufferedList if passed menuItem is added.
	 * @param menuItem
	 */
	
	public void optimizeMenuItemToBufferList(MenuItemRelations menuItem){
		Set<MenuItemRelations> processedMenuItems = new HashSet<MenuItemRelations>();
		List<MenuItemRelations> removeMenuItems = new ArrayList<MenuItemRelations>();
		boolean isAddMenuItem = false;
		for(Integer orderItemIndex : menuItem.getOrderItemsIndexes()){
			if(bufferedItemPriceLists[orderItemIndex].getItemPricesList().isEmpty()){
				 addMenuItemToBufferedList(menuItem);
				 return;
			}else{
				for(MenuItemRelations bufferedItemRelation : bufferedItemPriceLists[orderItemIndex].getItemPricesList()){
					if(processedMenuItems.add(bufferedItemRelation)){
						// If passed MenuItem is a meal of items contains items those are already in bufferedList and 
						// if MealCost is less than one in Buffered list. Remove more dearer menu item from buffered list.
						if(menuItem.getOrderItemsIndexes().containsAll(bufferedItemRelation.getOrderItemsIndexes())){
							if(menuItem.getPrice() < bufferedItemRelation.getPrice()){
								removeMenuItems.add(bufferedItemRelation);								
							}else if(bufferedItemRelation.getOrderItemsIndexes().containsAll(menuItem.getOrderItemsIndexes())){
								continue;
							}
							addMenuItemToBufferedList(menuItem);
							break;
						}else if(bufferedItemRelation.getOrderItemsIndexes().containsAll((menuItem.getOrderItemsIndexes()))){
							if(menuItem.getPrice() > bufferedItemRelation.getPrice()){
								break;
							}else{
								addMenuItemToBufferedList(menuItem);
								return;
							}
						}else{
							isAddMenuItem =true;
						}
					}
					
				}
			}			
		}
		removeMenuItemsFromBuffer(removeMenuItems);
		if(isAddMenuItem){
			addMenuItemToBufferedList(menuItem);
		}
	}
	
	/***
	 * Remove redundant Menu Items from buffered list along with decreasing the count in orderItemCountInMenu
	 * @param removeMenuItems
	 */
	public void removeMenuItemsFromBuffer(List<MenuItemRelations> removeMenuItems){
		for(MenuItemRelations menuItemRelations : removeMenuItems){
			for(Integer orderItemIndex : menuItemRelations.getOrderItemsIndexes()){
				bufferedItemPriceLists[orderItemIndex].getItemPricesList().remove(menuItemRelations);
				orderItemCountInMenu[orderItemIndex]--;
			}
		}
	}
	
	/**
	 * 
	 * @param requiredOnItemIndices Item Indices on which required optimum Set of MenuItems required
	 * @param processedMenuItems All ready processesMenuItems to reduce reworking on MenuItem
	 * @return
	 */
	
	public OrderItemList getOptimumMenuItem(List<Integer> requiredOnItemIndices, Set<MenuItemRelations> processedMenuItems){
		if(requiredOnItemIndices == null || requiredOnItemIndices.isEmpty()){
			return null;
		}
		if(processedMenuItems == null){
			processedMenuItems = new HashSet<MenuItemRelations>();
		}
		Set<MenuItemRelations> processingMenuItems = new HashSet<MenuItemRelations>(processedMenuItems);
		double minimumOrderListPrice = Double.MAX_VALUE;
		OrderItemList optimizedPriceList = null;
		for(MenuItemRelations bufferedMenuItem : bufferedItemPriceLists[requiredOnItemIndices.get(0)].getItemPricesList()){
			if(processingMenuItems.add(bufferedMenuItem)){
				List<Integer> pendingImpactedIndices = collectionsSubtract(requiredOnItemIndices, bufferedMenuItem.getOrderItemsIndexes());
				OrderItemList optimizedPendingIndicesList = null;
				if(pendingImpactedIndices.size() > 0){
					optimizedPendingIndicesList = getOptimumMenuItem(pendingImpactedIndices,processingMenuItems);
				}
				double pendingItemIndicesPrice = 0;
				if(optimizedPendingIndicesList == null){
					optimizedPendingIndicesList =  new OrderItemList();				
				}else{
					pendingItemIndicesPrice = optimizedPendingIndicesList.getTotalPrice();
				}
				double currentPrice = bufferedMenuItem.getPrice() + pendingItemIndicesPrice;
				if(currentPrice < minimumOrderListPrice){
					minimumOrderListPrice = currentPrice;
					optimizedPendingIndicesList.getItemPricesList().add(bufferedMenuItem);
					optimizedPendingIndicesList.setTotalPrice(currentPrice);
					optimizedPriceList = optimizedPendingIndicesList;
				}
				
				processingMenuItems.remove(bufferedMenuItem);
			}else{
				continue;
			}			
		}
		return optimizedPriceList;
	}
	

	/**
	 * Return a new collection by removing elements of dest from source
	 * @param source
	 * @param dest
	 * @return
	 */
	public List<Integer> collectionsSubtract(List<Integer> source, List<Integer> dest){
		List<Integer> result = new ArrayList<Integer>();
		for(int sourceIndex : source){
			if(!dest.contains(sourceIndex)){
				result.add(sourceIndex);
			}
		}
		return result;
	}
	

	
}
