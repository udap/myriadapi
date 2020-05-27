package io.chainmind.myriad.domain.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
	// order id
	private String id;
	// total amount in cent (x100)
	private int amount;
	// total quantity
	private int quantity;
	// order items, key is product SKU
	private Map<String, OrderItem> items;

	public Order addItem(OrderItem item) {
		if (items == null)
			items = new HashMap<>();
		items.put(item.getSku(),item);
		return this;
	}
	
	public boolean hasSku(String sku) {
		if (items == null)
			return false;
		return items.containsKey(sku);
	}
	
	public int countSku(String sku) {
		if (items == null || !items.containsKey(sku))
			return 0;
		return items.get(sku).getQuantity();
	}
	
	public boolean hasItemsInCategory(String category) {
		if (items == null)
			return false;
		return items.values().stream().anyMatch(item->Objects.equals(item.getCategory(), category));		
	}
	
	@Data
	static class OrderItem {
		private String sku;
		private String product;
		private String category;
		// real price x 100
		private int price;
		// number of product
		private int quantity;
	}
}
