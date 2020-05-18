package model;

public class Publisher {

	
	private String name;
	private String address;
	private String phone;
	
	
	public Publisher(String _name, String _address, String _phone){
		 setName(_name);
		 setAddress(_address);
		 setPhone(_phone);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
