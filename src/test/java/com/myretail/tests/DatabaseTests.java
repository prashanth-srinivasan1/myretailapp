package com.myretail.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.myretail.data.IProductDAO;
import com.myretail.models.Price;
import com.myretail.models.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = false, locations = { "classpath:/spring/AppContext.xml" })
public class DatabaseTests {

	@Resource
	private IProductDAO productDAO;
	
	@Test
	public void testInitialContent() 
	{
		// Get an item from the database (id=1) and verify that the values are correct
		Long[] ids = {1L};
		List<Product> pList = productDAO.getProductsByIds(Arrays.asList(ids));
		assertTrue(pList.size() == 1);
		Product p = pList.get(0);
		
		assertTrue(p.getCategory() != null && p.getCategory().equals("baby"));
		assertTrue(p.getPrice() != null && p.getPrice().getPrice() != null && p.getPrice().getPrice().equals(199.99));
		assertTrue(p.getSku() != null && p.getSku().equals("AEX143"));
		assertTrue(p.getProdName()!= null && p.getProdName().equals("Stroller"));
		
	}
	
	@Test
	public void insertAndVerifyData()
	{
		Product product = generateTestProduct();
		
		// Verify that the ids of the product and price are null before insertion
		assertNull(product.getId());
		assertNull(product.getPrice().getId());
		
		productDAO.persistProduct(product);
		
		// Verify that the ids of the product and price are not null after insertion
		assertNotNull(product.getId());
		assertNotNull(product.getPrice().getId());
		
		// Verify that the ID mapping works and that both ids are same
		assertEquals(product.getId(), product.getPrice().getId());
		
		// Check the number of items in database after insertion
		List<Product> pList = productDAO.getAllProducts(null, null);
		assertTrue(pList.size() == 4);
		
		/* Retrieve new product from the database and check individual fields
		 * from Product and Price classes
		 */
		
	}
		
	public Product generateTestProduct()
	{
		Product product = new Product();
		Price price = new Price();
		
		price.setPrice(45.00);
		
		product.setProdName("Test Product");
		product.setCategory("Test Category");
		product.setSku("Test Sku");
		product.setPrice(price);
		product.setLastUpdated(new Date());
		
		price.setProduct(product);
		
		return product;
	}
}
