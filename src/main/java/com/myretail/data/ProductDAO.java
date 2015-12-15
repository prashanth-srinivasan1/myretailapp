package com.myretail.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myretail.models.Product;

@Transactional
public class ProductDAO implements IProductDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public ProductDAO() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductsByIds(List<Long> ids) {
		List<Product> resultList = new ArrayList<Product>();
		try {
			resultList = entityManager
					.createQuery("select p from Product p where p.id in :ids")
					.setParameter("ids", ids).getResultList();
		} finally {
			entityManager.close();
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getProductsByCategory(String category, String start,
			String size) {
		List<Product> resultList = new ArrayList<Product>();
		try {
			Query q = entityManager.createQuery(
					"select p from Product p where p.category = :category")
					.setParameter("category", category);
			try {
				q.setFirstResult(Integer.valueOf(start));
				q.setMaxResults(Integer.valueOf(size));
			} catch (NumberFormatException e) {
				// Fetch all results if there is a conversion error
				q.setFirstResult(0);
				q.setMaxResults(0);
			}
			resultList = q.getResultList();
		} finally {
			entityManager.close();
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProducts(String start, String size) {
		List<Product> resultList = new ArrayList<Product>();
		try {
			Query q = entityManager.createQuery("select p from Product p");
			try {
				q.setFirstResult(Integer.valueOf(start));
				q.setMaxResults(Integer.valueOf(size));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// Fetch all results if there is a conversion error
				q.setFirstResult(0);
				q.setMaxResults(0);
			}
			resultList = q.getResultList();
		} finally {
			entityManager.close();
		}
		return resultList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void persistProduct(Product product) {
		try {
			if (product.getPrice() != null)
				product.getPrice().setProduct(product);
			entityManager.persist(product);
		} finally {
			entityManager.close();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeProduct(Long id) {
		try {
			List<Long> ids = new ArrayList<Long>();
			ids.add(id);
			List<Product> product = getProductsByIds(ids);
			if (product != null && !product.isEmpty())
				entityManager.remove(product.get(0));
		} finally {
			entityManager.close();
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateProduct(Product product) {
		try {
			List<Long> ids = new ArrayList<Long>();
			ids.add(product.getId());
			Product p = getProductsByIds(ids).get(0);
			// Update fields
			p.setCategory(product.getCategory());
			p.setLastUpdated(product.getLastUpdated());
			p.setProdName(product.getProdName());
			p.setSku(product.getSku());
			if (product.getPrice() != null)
			{
				p.getPrice().setPrice(product.getPrice().getPrice());
			}
			else
			{
				p.setPrice(null);
			}
			
			entityManager.flush();
		} finally {
			entityManager.close();
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}