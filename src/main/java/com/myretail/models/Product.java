package com.myretail.models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "PRODUCT")
@XmlRootElement(name="product")
@XmlType(propOrder={"prodName","sku","category","price","lastUpdated"})
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	@Column(name = "sku")
	private String sku;
	@Column(name = "prod_name")
	private String prodName;
	@Column(name = "category")
	private String category;
	@Column(name = "last_updated")
	private Date lastUpdated;
	@OneToOne(cascade=CascadeType.ALL, mappedBy="product", orphanRemoval=true)
	private Price price;

	@XmlAttribute
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name="sku")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@XmlElement(name="productname")
	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	@XmlElement(name="category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement(name="lastupdated")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@XmlElement(name="price")
	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	
}
