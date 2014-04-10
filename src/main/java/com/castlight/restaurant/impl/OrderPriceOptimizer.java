package com.castlight.restaurant.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.castlight.restaurant.model.OrderItemList;
import com.castlight.restaurant.model.MenuItemRelations;
import com.castlight.restaurant.model.RestaurantOrderSheet;

public class OrderPriceOptimizer {
	Map<Integer, RestaurantOrderSheet> restaurantOrderSheetMap = null;
	BufferedReader reader;
	Map<String,Integer> orderItems;
		
	/**
	 * Initialize Restaurant Map to Order Sheet, Map orderItems to indices.
	 * @param args
	 */
	public OrderPriceOptimizer(String...args){
		restaurantOrderSheetMap = new HashMap<Integer, RestaurantOrderSheet>();
		try{
			orderItems = new HashMap<String, Integer>();
			for(int k = 1; k < args.length; k++){
				orderItems.put(args[k], k-1);
			}
			FileReader fileReader = new FileReader(new File(args[0]));			
			reader = new BufferedReader(fileReader);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Read Menu Lines into respective Restaurant Order Sheet
	 * @return
	 * @throws Exception
	 */
	public String publishOptimizedRestaurantPrice() throws Exception{
		String restaurantMenuLine = reader.readLine();
		while(restaurantMenuLine != null){
			String[] menuLineItems = restaurantMenuLine.split(",");			
			MenuItemRelations menuLineItemPrice = getMenuLineItemPrice(menuLineItems);
			restaurantMenuLine = reader.readLine();
			if(menuLineItemPrice != null){
				Integer restaurantId = Integer.parseInt(menuLineItems[0].trim());
				RestaurantOrderSheet restaurantOrderSheet = restaurantOrderSheetMap.get(restaurantId);
				if(restaurantOrderSheet == null){
					restaurantOrderSheet = new RestaurantOrderSheet(orderItems.size());
					restaurantOrderSheetMap.put(restaurantId, restaurantOrderSheet);
				}
				restaurantOrderSheet.optimizeMenuItemToBufferList(menuLineItemPrice);
			}else{
				continue;
			}
		}		
		return getOptimizedRestaurantPrice();
	}

	/**
	 * Iterate across restaurants and get Minimized price restaurant
	 * @return
	 */
	
	private String getOptimizedRestaurantPrice() {
		int minRestId = -1;
		double minPrice = Double.MAX_VALUE;
		for(Map.Entry<Integer, RestaurantOrderSheet> restaurantOrderMapEntry : restaurantOrderSheetMap.entrySet()){
			RestaurantOrderSheet restaurantOrderSheet = restaurantOrderMapEntry.getValue();
			//If all ordered items avail at restaurant.
			if(restaurantOrderSheet.isAllItemsAvail()){				
				List<Integer>lsOrderItemIndices = Arrays.asList(orderItems.values().toArray(new Integer[1]));
				OrderItemList optimizedRestaurantOrder = restaurantOrderSheet.getOptimumMenuItem(lsOrderItemIndices, null);
				if(minPrice > optimizedRestaurantOrder.getTotalPrice()){
					minPrice = optimizedRestaurantOrder.getTotalPrice();
					minRestId = restaurantOrderMapEntry.getKey();
				}
			}
		}
		if(minRestId != -1){
			return minRestId+", "+minPrice;
		}else{
			return "null";
		}
	}	

	//Parse Menu Line Items as String to MenuItemRelations object
	public MenuItemRelations getMenuLineItemPrice(String... menuLineItems){
		MenuItemRelations menuLineItemRelations = new MenuItemRelations();
		for(int k=2; k < menuLineItems.length; k++){
			String itemName = menuLineItems[k].trim();
			Integer orderItemIndex = orderItems.get(itemName);
			if(orderItemIndex == null){
				continue;
			}
			menuLineItemRelations.addOrderItemIndex(orderItemIndex);
		}
		if(menuLineItemRelations.getOrderItemsIndexes().size() > 0){
			double menuLinePrice = Double.parseDouble(menuLineItems[1].trim());
			menuLineItemRelations.setPrice(menuLinePrice);
			return menuLineItemRelations;
		}
		return null;
	}
	
	public static String getOptimizedRestaurantPrice(String...args){
		try{
			if(args.length > 1){
				OrderPriceOptimizer optimizer = new OrderPriceOptimizer(args);
				return optimizer.publishOptimizedRestaurantPrice();
			}else{
				return "null";
			}		
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public static void main(String... args){
		System.out.println(getOptimizedRestaurantPrice(args));		
	}
	
	
}
