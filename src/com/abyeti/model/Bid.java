/**
 * 
 */
package com.abyeti.model;

import com.abyeti.constant.DateTime;

/**
 * @author Pushpendra
 *
 */
public class Bid {
	
	private String bid_Id;
	private String buyer_Id;
	private Double buyer_Price;
	private String product_Id;
	private DateTime bid_Time;
	/**
	 * @return the bid_Id
	 */
	public String getBid_Id() {
		return bid_Id;
	}
	/**
	 * @param bid_Id the bid_Id to set
	 */
	public void setBid_Id(String bid_Id) {
		this.bid_Id = bid_Id;
	}
	/**
	 * @return the buyer_Id
	 */
	public String getBuyer_Id() {
		return buyer_Id;
	}
	/**
	 * @param buyer_Id the buyer_Id to set
	 */
	public void setBuyer_Id(String buyer_Id) {
		this.buyer_Id = buyer_Id;
	}
	/**
	 * @return the buyer_Price
	 */
	public Double getBuyer_Price() {
		return buyer_Price;
	}
	/**
	 * @param buyer_Price the buyer_Price to set
	 */
	public void setBuyer_Price(Double buyer_Price) {
		this.buyer_Price = buyer_Price;
	}
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
	 * @return the bid_Time
	 */
	public DateTime getBid_Time() {
		return bid_Time;
	}
	
	/**
	 * @param bid_Time the bid_Time to set
	 */
	public void setBid_Time(DateTime bid_Time) {
		this.bid_Time = bid_Time;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bid [bid_Id=" + bid_Id + ", buyer_Id=" + buyer_Id
				+ ", buyer_Price=" + buyer_Price + ", product_Id=" + product_Id
				+ ", bid_Time=" + bid_Time + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bid_Id == null) ? 0 : bid_Id.hashCode());
		result = prime * result
				+ ((bid_Time == null) ? 0 : bid_Time.hashCode());
		result = prime * result
				+ ((buyer_Id == null) ? 0 : buyer_Id.hashCode());
		result = prime * result
				+ ((buyer_Price == null) ? 0 : buyer_Price.hashCode());
		result = prime * result
				+ ((product_Id == null) ? 0 : product_Id.hashCode());
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
		Bid other = (Bid) obj;
		if (bid_Id == null) {
			if (other.bid_Id != null)
				return false;
		} else if (!bid_Id.equals(other.bid_Id))
			return false;
		if (bid_Time == null) {
			if (other.bid_Time != null)
				return false;
		} else if (!bid_Time.equals(other.bid_Time))
			return false;
		if (buyer_Id == null) {
			if (other.buyer_Id != null)
				return false;
		} else if (!buyer_Id.equals(other.buyer_Id))
			return false;
		if (buyer_Price == null) {
			if (other.buyer_Price != null)
				return false;
		} else if (!buyer_Price.equals(other.buyer_Price))
			return false;
		if (product_Id == null) {
			if (other.product_Id != null)
				return false;
		} else if (!product_Id.equals(other.product_Id))
			return false;
		return true;
	}
	
}
