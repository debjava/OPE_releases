package com.dnb.agreement.bean;

/**This class is used as bean to maintain
 * the customer information.
 * @author Debadatta Mishra
 *
 */
public class CustomerIdBean
{
	private String customerId;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	private String customerName;
	private String address;
	public String getCustomerName() 
	{
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}	
