package com.myretail.bus;

import java.util.List;

import com.myretail.models.Product;

public interface IProductBO {

	public List<Product> getProductsByIds(List<Long> ids);

	public List<Product> getProductsByCategory(String category, String start, String size);

	public List<Product> getAllProducts(String start, String size);

	public void persistProduct(Product product);

	public void removeProduct(Long id);
	
	public void updateProduct(Product product);
}
