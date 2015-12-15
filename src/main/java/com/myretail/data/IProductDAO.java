package com.myretail.data;

import java.util.List;

import com.myretail.models.Product;

public interface IProductDAO {

	// get products by id(s)
	public List<Product> getProductsByIds(List<Long> ids);

	// get all products by category
	public List<Product> getProductsByCategory(String category, String start,
			String size);

	// retrieve all products
	public List<Product> getAllProducts(String start, String size);

	// save product
	public void persistProduct(Product product);

	// Remove product
	public void removeProduct(Long id);

	// Update product
	public void updateProduct(Product product);

}
