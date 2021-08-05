package com;

import com.customers.*;

public class Main {
	public static void main(String [] args) {
		Customer b = new Customer();
		CustomerDAO a = new CustomerDAO(b);
		a.getConnected();
	}
}
