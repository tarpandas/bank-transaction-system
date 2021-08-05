package com.customers;

import java.sql.*;
import java.util.Scanner;

public class CustomerDAO {
	Customer customer;
	public CustomerDAO(Customer b) {
		customer = b;
	}
	public void getConnected() {
		try {
			String url = "jdbc:oracle:thin:password/TARPAN@localhost:1521:XE";
			String uname = "TARPAN";
			String pword = "password";
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,uname,pword);
			if(con==null) {
				System.out.println("Connection not successful");
			}else {
				System.out.println("Connection successful");
			}
			
			int choice=0;
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your choice of operation: \n1. Save details\n2. Display All\n3. Do a Transaction\n4. Quit");
			choice = sc.nextInt();
			switch(choice) {
				case 1:
					saveCustomer(con);
					break;
				case 2:
					displayAll(con);
					break;
				case 3:
					boolean a = doTransaction(con);
					if(a==true) {
						System.out.println("Transaction successful");
					}
					break;
				default:
					System.out.println("__EXIT__");
			}
			sc.close();
			con.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void displayAll(Connection con) {
		try {
			System.out.println("---Display the employee database---");
	        Statement st = con.createStatement();
	        ResultSet rs = st.executeQuery("select * from Customers");
	        int c2; double c3;
	        String c1;
	        while(rs.next()){
	            c1 = rs.getString(1);
	            c2 = rs.getInt(2);
	            c3 = rs.getDouble(3);
	            System.out.println("'"+c1+"' '"+ c2 +"' '"+ c3+"'");
	        }
	        st.close();        
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void saveCustomer(Connection con) {
		try {
			enterData();
			CallableStatement st = con.prepareCall("{call set_cus(?, ?, ?)}");
			st.setString(1,customer.getName());
			st.setInt(2,customer.getAccountNo());
			st.setDouble(3, customer.getBalance());
			st.executeQuery();
			System.out.println("Data inserted!");
			st.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void enterData() {
		Scanner num = new Scanner(System.in);
		Scanner alpha = new Scanner(System.in);
		System.out.print("Enter the Customer Account no.:");
		int accountNo = num.nextInt();
		customer.setAccountNo(accountNo);
		System.out.print("Enter the Customer Name:");
		String name = alpha.nextLine();
		customer.setName(name);
		System.out.print("Enter the Customer Balance:");
		double balance = num.nextDouble();
		customer.setBalance(balance);
		num.close();
		alpha.close();
	}
	public boolean doTransaction(Connection con){
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter the sender's account number: ");
			int a = sc.nextInt();
			System.out.println("Enter the reciever's account number: ");
			int b = sc.nextInt();
			System.out.println("Enter the amount to transfer: ");
			double c = sc.nextDouble();
			
			checkBal(a, c, con);
			
			CallableStatement st = con.prepareCall("{call set_tran(?, ?, ?)}");
			st.setInt(1,a);
			st.setInt(2,b);
			st.setDouble(3,c);
			
			ResultSet comp = st.executeQuery();
			System.out.println("Data inserted!");
			sc.close();
			st.close();
			if(comp!=null)
				return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	private void checkBal(int a, double c, Connection con) {
		try {
			double bal;
			CallableStatement st = con.prepareCall("{call check_bal(?, ?)}");
			st.setInt(1,a);
			st.registerOutParameter(2,Types.DOUBLE);
			st.executeQuery();
			bal = st.getDouble(2);
			if(bal<c) {
				System.out.println("Balance too low for transaction.\n");
				System.out.println("Available balance: "+bal+",	Transfer amount: "+c);
				System.exit(0);
			}
			st.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
