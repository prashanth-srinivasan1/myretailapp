package com.myretail.mvc;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.myretail.dto.Product;
import com.myretail.dto.Search;
import com.myretail.models.Price;
import com.myretail.models.Products;

// Main Controller for the MVC application
@Controller
@RequestMapping("/")
public class ProductContoller {

	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(method = RequestMethod.GET)
	public String home(ModelMap model) {
		model.addAttribute("product", new Product());
		return "home";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addProductForm(ModelMap model) {
		model.addAttribute("product", new Product());
		return "add";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addProductSubmit(@ModelAttribute("product") Product product,
			ModelMap model) {
		com.myretail.models.Product newProduct = new com.myretail.models.Product();
		newProduct.setProdName(product.getName());
		newProduct.setCategory(product.getCategory());
		newProduct.setSku(product.getName());
		com.myretail.models.Price price = new Price();
		try {
			price.setPrice(Double.valueOf(product.getPrice()));
		} catch (NumberFormatException e) {
			model.addAttribute("error",
					"Error : Invalid Price : " + e.getLocalizedMessage());
			return "add";
		}
		newProduct.setPrice(price);
		newProduct.setLastUpdated(new Date());

		ResponseEntity<Response> addResponse = restTemplate.postForEntity(
				"http://localhost:9090/myretailapp/api/product", newProduct,
				Response.class);
		if (addResponse.getStatusCode().equals(HttpStatus.CREATED))
			model.addAttribute("success", "Product created Successfully");

		return "add";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String editProductForm(ModelMap model) {
		model.addAttribute("search", new Search());
		model.addAttribute("product", new Product());
		return "edit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editProduct(@ModelAttribute("search") Search search,
			ModelMap model) {
		String id = search.getIds();
		if (id == null || id.isEmpty()) {
			model.addAttribute("error", "Please enter a valid Id to edit");
			model.addAttribute("product", new Product());
			return "edit";
		}

		Products products = restTemplate.getForObject(
				"http://localhost:9090/myretailapp/api/product/" + id,
				Products.class);

		if (products != null && products.getProdList() != null
				&& !products.getProdList().isEmpty()) {
			if (products.getProdList().size() != 1) {
				model.addAttribute("error", "Please enter a valid Id to edit");
				model.addAttribute("product", new Product());
				return "edit";
			} else {
				com.myretail.models.Product p = products.getProdList().get(0);
				Product product = new Product();
				product.setId(id);
				product.setName(p.getProdName());
				product.setSku(p.getSku());
				product.setCategory(p.getCategory());
				if (p.getPrice() != null && p.getPrice().getPrice() != null)
					product.setPrice(p.getPrice().getPrice().toString());
				model.addAttribute("product", product);
			}
		} else {
			model.addAttribute("error", "No Matching products were found");
			model.addAttribute("product", new Product());
			return "edit";
		}
		return "editview";
	}

	@RequestMapping(value = "/editsubmit", method = RequestMethod.POST)
	public String editProductSubmit(@ModelAttribute("search") Search search,
			@ModelAttribute("product") Product product, ModelMap model) {
		com.myretail.models.Product p = new com.myretail.models.Product();
		p.setId(Long.valueOf(product.getId()));
		p.setLastUpdated(new Date());
		p.setSku(product.getSku());
		p.setProdName(product.getName());
		p.setCategory(product.getCategory());
		if (product.getPrice() != null) {
			Price price = new Price();
			try {
				price.setPrice(Double.valueOf(product.getPrice()));
			} catch (NumberFormatException e) {
				model.addAttribute("error", "Price is invalid");
				model.addAttribute("product", product);
				return "editview";
			}	
			p.setPrice(price);
		}

		try {
		restTemplate.put("http://localhost:9090/myretailapp/api/product/", p);
		}
		catch (RestClientException ex)
		{
			model.addAttribute("error", "Unable to update product. Please try again.");
			model.addAttribute("product", product);
			return "editview";
		}

		model.addAttribute("success", "Product was updated successfully");
		return "editview";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteProductForm(ModelMap model) {
		model.addAttribute("search", new Search());
		return "delete";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteProductApi(@ModelAttribute("search") Search search,
			ModelMap model) {

		if (search.getIds() == null || search.getIds().isEmpty()) {
			model.addAttribute("error", "Please enter a valid Id to delete");
			return "delete";
		}

		try {
			restTemplate
					.delete("http://localhost:9090/myretailapp/api/product/"
							+ search.getIds());
		} catch (RestClientException e) {
			model.addAttribute(
					"error",
					"Unable to delete resource :" + search.getIds() + " "
							+ e.getMessage());
			return "delete";
		}

		model.addAttribute("success", "Resource :" + search.getIds()
				+ " successfully deleted");

		return "delete";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchProductForm(ModelMap model) {
		model.addAttribute("search", new Search());
		return "search";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchProductSubmit(@ModelAttribute("search") Search search,
			ModelMap model) {
		Products products = new Products();
		if (search.getCategory().isEmpty() && !search.getIds().isEmpty()) {
			System.out.println("Search by ids");
			String ids = search.getIds();
			ids = ids.replace(",", ":");
			products = restTemplate.getForObject(
					"http://localhost:9090/myretailapp/api/product/" + ids,
					Products.class);
		} else if (search.getIds().isEmpty() && !search.getCategory().isEmpty()) {
			System.out.println("Search by category");
			products = restTemplate.getForObject(
					"http://localhost:9090/myretailapp/api/product/category/"
							+ search.getCategory(), Products.class);
		} else {
			model.addAttribute("error",
					"Error : No products were found matching these criteria!");
		}

		if (products != null && products.getProdList() != null
				&& !products.getProdList().isEmpty()) {
			model.addAttribute("result", products.getProdList());
		} else {
			model.addAttribute("error",
					"No products were found for these search criteria!");
		}

		return "results";
	}
}
