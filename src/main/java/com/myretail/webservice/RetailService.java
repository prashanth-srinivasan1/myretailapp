package com.myretail.webservice;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

import com.myretail.bus.IProductBO;
import com.myretail.models.Product;
import com.myretail.models.Products;

@Component
@Path("/product")
public class RetailService {

	@Inject
	private IProductBO productBO;

	public IProductBO getProductBO() {
		return productBO;
	}

	public void setProductBO(IProductBO productBO) {
		this.productBO = productBO;
	}

	// Get a list of all products in the database. Can add parameters to
	// restrict the result size
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllProducts(@QueryParam("start") String start,
			@QueryParam("size") String size) {
		List<Product> pList = productBO.getAllProducts(start, size);
		Products output = new Products();
		output.setProdList(pList);
		return Response.status(200).entity(output).build();
	}

	// Get a single product by id or get multiple products by semicolon
	// separated ids
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getProductById(@PathParam("id") String ids) {
		Products output = new Products();
		List<Long> idList = new ArrayList<Long>();
		for (String id : ids.split(":")) {
			try {
				Long i = Long.valueOf(id);
				idList.add(i);
			} catch (NumberFormatException e) {
				// Ignore illegal ids
			}
		}

		List<Product> pList = productBO.getProductsByIds(idList);
		output.setProdList(pList);
		return Response.status(200).entity(output).build();
	}

	// Get a list of products for a category.
	@GET
	@Path("/category/{category}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getProductsByCategory(
			@PathParam("category") String category,
			@QueryParam("start") String start, @QueryParam("size") String size) {
		List<Product> pList = productBO.getProductsByCategory(category, start,
				size);
		Products output = new Products();
		output.setProdList(pList);
		return Response.status(200).entity(output).build();
	}

	@DELETE
	@Path("/{id}")
	public Response removeProduct(@PathParam("id") String id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.valueOf(id));
		List<Product> result = productBO.getProductsByIds(ids);
		if (result == null || result.isEmpty()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			productBO.removeProduct(ids.get(0));
		}
		return Response.noContent().status(200).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addProduct(Product newProduct) {
		productBO.persistProduct(newProduct);
		return Response.created(URI.create("/product/" + newProduct.getId()))
				.build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response editProduct(Product updatedProduct, @Context Request request) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(updatedProduct.getId());
		List<Product> result = productBO.getProductsByIds(ids);
		if (result == null || result.isEmpty()) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			Product oldProduct = result.get(0);

			// Make sure that the product being updated is not stale
			EntityTag tag = new EntityTag(Integer.toString(oldProduct
					.hashCode()));
			Date timestamp = new Date();
			ResponseBuilder builder = request.evaluatePreconditions(timestamp,
					tag);

			if (builder != null)
				return builder.build();

			productBO.updateProduct(updatedProduct);

			builder = Response.noContent().status(Status.OK);
			return builder.build();
		}
	}
}
