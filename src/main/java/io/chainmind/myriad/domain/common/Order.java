package io.chainmind.myriad.domain.common;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {
	@NotBlank
	private String id;
	// total amount in cent (x100)
	private Integer amount;
	// total quantity
	private Integer quantity;
	// order items
	private List<OrderItem> items;
	
	@Data
	static class OrderItem {
		@NotBlank
		private String sku;
		private String product;
		private String category;
		// real price x 100
		@PositiveOrZero
		private Integer price;
		// number of product
		@Positive
		private Integer quantity;
	}
	
}
