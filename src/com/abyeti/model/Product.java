/**
 * 
 */
package com.abyeti.model;

/**
 * @author Pushpendra
 *
 */
public class Product {
	
	private String product_Id;
	private String product_Name;
	private String product_Disc;
	private Double initial_Bid;
	private String last_Bid_Time;
	
	private String seller_Id;

	/**
	 * @return the product_Id
	 */
	public String getProduct_Id() {
		return product_Id;
	}

	/**
	 * @param product_Id the product_Id to set
	 */
	public void setProduct_Id(String product_Id) {
		this.product_Id = product_Id;
	}

	/**
	 * @return the product_Name
	 */
	public String getProduct_Name() {
		return product_Name;
	}

	/**
	 * @param product_Name the product_Name to set
	 */
	public void setProduct_Name(String product_Name) {
		this.product_Name = product_Name;
	}

	/**
	 * @return the product_Disc
	 */
	public String getProduct_Disc() {
		return product_Disc;
	}

	/**
	 * @param product_Disc the product_Disc to set
	 */
	public void setProduct_Disc(String product_Disc) {
		this.product_Disc = product_Disc;
	}

	/**
	 * @return the initial_Bid
	 */
	public Double getInitial_Bid() {
		return initial_Bid;
	}

	/**
	 * @param initial_Bid the initial_Bid to set
	 */
	public void setInitial_Bid(Double initial_Bid) {
		this.initial_Bid = initial_Bid;
	}

	/**
	 * @return the last_Bid_Time
	 */
	public String getLast_Bid_Time() {
		return last_Bid_Time;
	}

	/**
	 * @param last_Bid_Time the last_Bid_Time to set
	 */
	public void setLast_Bid_Time(String last_Bid_Time) {
		this.last_Bid_Time = last_Bid_Time;
	}

	/**
	 * @return the seller_Id
	 */
	public String getSeller_Id() {
		return seller_Id;
	}

	/**
	 * @param seller_Id the seller_Id to set
	 */
	public void setSeller_Id(String seller_Id) {
		this.seller_Id = seller_Id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((initial_Bid == null) ? 0 : initial_Bid.hashCode());
		result = prime * result
				+ ((last_Bid_Time == null) ? 0 : last_Bid_Time.hashCode());
		result = prime * result
				+ ((product_Disc == null) ? 0 : product_Disc.hashCode());
		result = prime * result
				+ ((product_Id == null) ? 0 : product_Id.hashCode());
		result = prime * result
				+ ((product_Name == null) ? 0 : product_Name.hashCode());
		result = prime * result
				+ ((seller_Id == null) ? 0 : seller_Id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (initial_Bid == null) {
			if (other.initial_Bid != null)
				return false;
		} else if (!initial_Bid.equals(other.initial_Bid))
			return false;
		if (last_Bid_Time == null) {
			if (other.last_Bid_Time != null)
				return false;
		} else if (!last_Bid_Time.equals(other.last_Bid_Time))
			return false;
		if (product_Disc == null) {
			if (other.product_Disc != null)
				return false;
		} else if (!product_Disc.equals(other.product_Disc))
			return false;
		if (product_Id == null) {
			if (other.product_Id != null)
				return false;
		} else if (!product_Id.equals(other.product_Id))
			return false;
		if (product_Name == null) {
			if (other.product_Name != null)
				return false;
		} else if (!product_Name.equals(other.product_Name))
			return false;
		if (seller_Id == null) {
			if (other.seller_Id != null)
				return false;
		} else if (!seller_Id.equals(other.seller_Id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Product [product_Id=" + product_Id + ", product_Name="
				+ product_Name + ", product_Disc=" + product_Disc
				+ ", initial_Bid=" + initial_Bid + ", last_Bid_Time="
				+ last_Bid_Time + ", seller_Id=" + seller_Id + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}

}
