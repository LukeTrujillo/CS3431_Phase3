/*
 *  Main.java
 * @date Feb 21, 2020
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main
 * @author Luke Trujillo
 * @date Feb 21, 2020
 */
public class Main {
	
	public static void main(String[] args) {
		Connection conn = null;
		
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@oracle.wpi.edu:1521:orcl", args[0], args[1]);
        } catch (SQLException e) {
            System.out.println("There was an error with connecting");
            e.printStackTrace();
            return;
        }
        
        if (conn != null) {
            System.out.println("connected to db");
        } else {
            System.out.println("did not connect");
        }
        
        Scanner scanner = new Scanner(System.in);
        

        if(args.length == 2) {
        	System.out.println("1 - Report Patients Basic Information");
        	System.out.println("2 - Report Doctors Basic Information");
        	System.out.println("3 - Report Admissions Information");
        	System.out.println("4 - Update Admissions Payment Information");
        	
        	return;
        } else if(1 == Integer.parseInt(args[2])) {
        	System.out.print("Enter Patient SSN (xxx-xx-xxxx): " );
        	String ssn = scanner.nextLine();
       
        	
        	String query = "SELECT * FROM Patient WHERE SSN='" + ssn + "'";
        	
        	try { 
        
        		ResultSet result = conn.createStatement().executeQuery(query);
        		result.next();
	        		System.out.println("Patient SSN: " + result.getString("SSN"));
		        	System.out.println("Patient First Name: " + result.getString("firstName"));
		        	System.out.println("Patients Last Name: " + result.getString("lastName"));
		        	System.out.println("Patent Address: " + result.getString("address"));
        	
        	} catch(SQLException e) {
        		e.printStackTrace();
        		
        		try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        } else if(2 == Integer.parseInt(args[2])) {
        	System.out.print("Enter Doctor ID: " );
        	int id = scanner.nextInt();
       
        	
        	String query = "SELECT * FROM Doctor WHERE ID=" + id;
        	
        	try { 
        
        		ResultSet result = conn.createStatement().executeQuery(query);
        		while(result.next()) {
	        		System.out.println("Doctor ID: " + result.getString("ID"));
		        	System.out.println("Doctor First Name: " + result.getString("firstName"));
		        	System.out.println("Doctor Last Name: " + result.getString("lastName"));
		        	System.out.println("Doctor Gender: " + result.getString("gender"));
        		}
        	
        	} catch(SQLException e) {
        		e.printStackTrace();
        		
        		try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        } else if(Integer.parseInt(args[2]) == 3) {
         	System.out.print("Enter Admission Number: " );
    		int id = scanner.nextInt();
   
        	
        	try {
				ResultSet admissionResult = conn.createStatement().executeQuery("SELECT * FROM Admission WHERE admissionNum="+ id);
				admissionResult.next();
				
				System.out.println("Admission Number: " + admissionResult.getInt("admissionNum"));
				System.out.println("Patient SSN: " + admissionResult.getString("patientSSN"));
				System.out.println("Admission Date (start date): " + admissionResult.getDate("admissionDate").toString());
				System.out.println("Total Payment: " + admissionResult.getInt("totalPayment"));
				
				ResultSet roomResult = conn.createStatement().executeQuery("SELECT * FROM StayIn WHERE admissionNum=" + admissionResult.getInt("admissionNum"));
				System.out.println("Rooms:");
			
				while(roomResult.next()) {
					System.out.print("\tRoom Number: " + roomResult.getInt("roomNum"));
					System.out.print("\tFrom: " + roomResult.getDate("startDate").toString());
					System.out.print("\tUntil: " + roomResult.getDate("endDate").toString() + "\n");
				} 
				ResultSet examineResult = conn.createStatement().executeQuery("SELECT DISTINCT doctorID FROM Examine WHERE admissionNum=" + admissionResult.getInt("admissionNum"));
				System.out.println("Doctors examined the patient in this admission:");
				while(examineResult.next()) {
					System.out.println("\tDoctor ID: " + examineResult.getInt("doctorID"));
				}
        	
        	} catch (SQLException e) {
				e.printStackTrace();
			}
        } else if(Integer.parseInt(args[2]) == 4) {
        	System.out.print("Enter Admission Number: " );
    		int id = scanner.nextInt();
    		
    		System.out.print("Enter new total payment: " );
    		int totalPayment = scanner.nextInt();
    		
    		
    		try {
				conn.createStatement().executeUpdate("UPDATE Admission SET totalPayment=" + totalPayment + " WHERE admissionNum=" + id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        
        try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        scanner.close();
	}
}
