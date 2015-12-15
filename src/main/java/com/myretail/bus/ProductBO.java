package com.myretail.bus;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Named;

import com.myretail.data.IProductDAO;
import com.myretail.models.Product;

@Named("productBO")
public class ProductBO implements IProductBO {

	@Resource(name = "ProductDAO")
	IProductDAO productDAO;

	@Override
	public List<Product> getProductsByIds(List<Long> ids) {
		return productDAO.getProductsByIds(ids);
	}

	@Override
	public List<Product> getProductsByCategory(String category, String start, String size) {
		return productDAO.getProductsByCategory(category, start, size);
	}

	@Override
	public List<Product> getAllProducts(String start, String size) {
		return productDAO.getAllProducts(start, size);
	}

	@Override
	public void persistProduct(Product product) {
		productDAO.persistProduct(product);
	}

	@Override
	public void removeProduct(Long id) {
		productDAO.removeProduct(id);
	}

	@Override
	public void updateProduct(Product product)
	{
		productDAO.updateProduct(product);
	}
}
