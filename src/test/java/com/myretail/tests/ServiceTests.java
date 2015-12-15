package com.myretail.tests;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.*;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.myretail.models.Product;
import com.myretail.models.Products;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(inheritLocations = false, locations = { "classpath:/spring/AppContext.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceTests {

	@Before
	public void setUp() {
		RestAssured.port = 9090;
		RestAssured.baseURI = "http://localhost";
	}

	// @GET tests

	// Get a single product by id and verify properties
	@Test
	public void test_1_SingleIdGet() {

		given().accept(MediaType.APPLICATION_XML).expect().statusCode(200).then()
				.body("products.product.productname", equalTo("Stroller"))
				.body("products.product.sku", equalTo("AEX143"))
				.body("products.product.price.price", equalTo("199.99"))
				.body("products.product.category", equalTo("baby")).when()
				.get("/myretailapp/api/product/1");
	
	}

	// Get multiple products based on id
	@Test
	public void test_2_MultipleIdGet() {
		String response = given().get("/myretailapp/api/product/1:2:3").asString();
		List<String> productIds = from(response).get("products.product.@id");
		// Verify that three products are returned
		Assert.assertEquals(productIds.size(), 3);
	}

	// @POST test
	
	// Add a new product - Test with JSON and XML payloads
	@Test
	public void test_3_ProductAdd() {
		int initSize, finalSize;
		initSize = getNumberOfItems();
		// Create a new product
		Product p = new Product();
		p.setCategory("Test Category");
		p.setProdName("Test Name");
		p.setSku("Test sku");

		// Insert a product as JSON
		given().contentType("application/json; charset=UTF-8").body(p).when()
				.post("/myretailapp/api/product").then().statusCode(201);

		// Insert a product as XML
		given().contentType("application/xml; charset=UTF-8")
				.body(p, ObjectMapperType.JAXB).when()
				.post("/myretailapp/api/product").then().statusCode(201);

		finalSize = getNumberOfItems();

		// Verify that two extra products are returned
		Assert.assertEquals(finalSize, initSize + 2);
	}

	// @DELETE test
	
	// Delete a product and verify that it is removed 
	@Test
	public void test_4_ProductDelete() {
		int initSize, finalSize;
		initSize = getNumberOfItems();
		
		given().delete("/myretailapp/api/product/5").then().statusCode(200);
		
		finalSize = getNumberOfItems();
		Assert.assertEquals(finalSize, initSize - 1);

	}
	
	// @PUT test
	
	// Update a product and verify that the change is reflected 
	
	@Test
	public void test_5_ProductUpdate()
	{
		// Get an existing product
		Product product = given().get("/myretailapp/api/product/1").as(Products.class).getProdList().get(0);
		
		product.setCategory("testing");
		
		// Modify and update the product
		given().contentType(MediaType.APPLICATION_XML).body(product).when()
		.put("/myretailapp/api/product").then().statusCode(200);
		
		given().accept(MediaType.APPLICATION_XML).expect().statusCode(200).then()
		.body("products.product.category", equalTo("testing")).when()
		.get("/myretailapp/api/product/1");
		
	}
	
	// Helper method to get a count of products in the database using the service
	public int getNumberOfItems() {
		// Get the initial number of products in the database
		String response = given().get("/myretailapp/api/product").asString();
		List<String> productIds = from(response).get("products.product.@id");
		int length = productIds.size();
		return length;
	}
}
