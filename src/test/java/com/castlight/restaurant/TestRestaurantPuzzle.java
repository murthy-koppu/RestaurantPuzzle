package com.castlight.restaurant;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;

import com.castlight.restaurant.impl.OrderPriceOptimizer;

public class TestRestaurantPuzzle {

	@Test
	public void test() {		 
		 Assert.assertEquals("Failed", "2, 11.5", OrderPriceOptimizer.getOptimizedRestaurantPrice("F:/Uploads/castlight/restaurant_puzzle_data/sample_data.csv","burger","tofu_log"));
		 Assert.assertEquals("Failed", "6, 6.0", OrderPriceOptimizer.getOptimizedRestaurantPrice("F:/Uploads/castlight/restaurant_puzzle_data/sample_data_1.csv","jalapeno_poppers","extreme_fajita"));
		 Assert.assertEquals("Failed", "4, 2.5", OrderPriceOptimizer.getOptimizedRestaurantPrice("F:/Uploads/castlight/restaurant_puzzle_data/sample_data_2.csv","fancy_european_water","jalapeno_poppers"));
		 Assert.assertEquals("Failed", "3, 8.0", OrderPriceOptimizer.getOptimizedRestaurantPrice("F:/Uploads/castlight/restaurant_puzzle_data/sample_data_3.csv","steak_salad_sandwich","chef_salad"));
		 Assert.assertEquals("Failed", "7, 11.5", OrderPriceOptimizer.getOptimizedRestaurantPrice("F:/Uploads/castlight/restaurant_puzzle_data/sample_data_3.csv","chef_salad","tofu_log","jalapeno_poppers"));
	}

}
