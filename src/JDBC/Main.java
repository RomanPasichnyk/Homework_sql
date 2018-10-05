package JDBC;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class Main {

	static Connection conn;

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {

		String dbUrl = "jdbc:mysql://localhost:3306/homework_sql?useSSL=false";
		String username = "root";
		String password = "pasitchnyk655696!";

		conn = DriverManager.getConnection(dbUrl, username, password);
		System.out.println("Connected? " + !conn.isClosed());

		String answer = "";
		countryFile();
		cityFile();
		personFile();
		
		while(!answer.equals("Exit")) {
			System.out.println("\t\t...:::< Menu >:::...\n");
			System.out.println("AddCountry \t\t| AddCity \t\t| AddPerson");
			System.out.println("ShowCountry \t\t| ShowCity \t\t| ShowPerson");
			System.out.println("ShowCountryID \t\t| ShowCityID \t\t| ShowPersonID");
			System.out.println("ShowCityPerson \t\t| ShowCityCountry \t| Exit");
			System.out.println("ShowPersonForName \t| EditPerson");
			answer = sc.next();
			switch(answer) {
				case "AddCountry": {
					addCountry();
					break;
				}
				case "AddCity": {
					addCity();
					break;
				}
				case "AddPerson": {
					addPerson();
					break;
				}
				case "ShowCountry": {
					showCountry();
					break;
				}
				case "ShowCity": {
					showCity();
					break;
				}
				case "ShowPerson": {
					showPerson();
					break;
				}
				case "ShowCountryID": {
					showCountryID();
					break;
				}
				case "ShowCityID": {
					showCityID();
					break;
				}
				case "ShowPersonID": {
					showPersonID();
					break;
				}
				case "ShowCountryPerson": {
					showCityPerson();
					break;
				}
				case "ShowCityCountry": {
					showCityCountry();
					break;
				}
				case "ShowPersonForName": {
					showPersonForName();
					break;
				}
				case "EditPerson": {
					editPerson();
					break;
				}
				
			}
		}
		
		conn.close();

	}

	private static void addCountry() throws SQLException {
		System.out.println("Enter country name");
		String name = sc.next();
		String query = "INSERT INTO country(name) VALUES(?);";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.executeUpdate();
		pstmt.close();
		System.out.println("Country successfully added!");
	}

	private static void addCity() throws SQLException {

		System.out.println("Enter city name");
		String nameCity = sc.next();
		System.out.println("Enter country name");
		String nameCountry = sc.next();
		String query = "SELECT * FROM country;";
		int country_id = 0;
		boolean check = false;
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (nameCountry.equals(rs.getString("name"))) {
				country_id = rs.getInt("id");
				check = true;
				break;
			} else
				check = false;
		}

		query = "INSERT INTO city(name, country_id) VALUES(?, ?);";
		if (check) {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, nameCity);
			pstmt.setInt(2, country_id);
			pstmt.executeUpdate();
			System.out.println("City successfully added!");
		} else
			System.out.println("Country not found!");
		pstmt.close();
	}

	private static void addPerson() throws SQLException {
		System.out.println("Enter first name");
		String firstName = sc.next();
		System.out.println("Enter last name");
		String lastName = sc.next();
		System.out.println("Enter age");
		int age = sc.nextInt();
		System.out.println("Enter city");
		String nameCity = sc.next();

		String query = "SELECT * FROM city";
		int city_id = 0;
		boolean check = false;
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (nameCity.equals(rs.getString("name"))) {
				city_id = rs.getInt("id");
				check = true;
				break;
			} else
				check = false;
		}

		query = "INSERT INTO person(first_name, last_name, age, city_id) VALUES(?, ?, ?, ?);";
		if (check) {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setInt(3, age);
			pstmt.setInt(4, city_id);
			pstmt.executeUpdate();
			System.out.println("Person successfully added!");
		} else
			System.out.println("City not found!");
		pstmt.close();
	}
	
	private static void showPerson() throws SQLException {
		String query = "SELECT p.*, c.* FROM person p JOIN city c ON c.id = p.city_id ORDER BY first_name ASC;";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"First Name: " + rs.getString("first_name") + "\t | " + 
					"Last Name: " + rs.getString("last_name") + "\t | " +
					"Age: " + rs.getInt("age") + "\t | " + 
					"City: " + rs.getString("name"));
		}
	}
	
	private static void showCity() throws SQLException {
		String query = "SELECT c.*, co.name AS country_name FROM city c JOIN country co ON co.id = c.country_id ORDER BY name DESC;";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"City: " + rs.getString("name") + "\t | " + 
					"Country: "+ rs.getString("country_name"));
		}
	}
	
	private static void showCountry() throws SQLException {
		String query = "SELECT * FROM country;";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"Country: " + rs.getString("name"));
		}
	}
	
	private static void showPersonID() throws SQLException {
		System.out.println("Enter person ID");
		int id = sc.nextInt();
		String query = "SELECT p.*, c.* FROM person p JOIN city c ON c.id = p.city_id WHERE p.id = " + id + ";";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"First Name: " + rs.getString("first_name") + "\t | " + 
					"Last Name: " + rs.getString("last_name") + "\t | " +
					"Age: " + rs.getInt("age") + "\t | " + 
					"City: " + rs.getString("name"));
		}
	}
	
	private static void showCityID() throws SQLException {
		System.out.println("Enter city ID");
		int id = sc.nextInt();
		String query = "SELECT c.*, co.name AS country_name FROM city c JOIN country co ON co.id = c.country_id WHERE c.id = " + id + ";";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"City: " + rs.getString("name") + "\t | " + 
					"Country: "+ rs.getString("country_name"));
		}
	}
	
	private static void showCountryID() throws SQLException {
		System.out.println("Enter country ID");
		int id = sc.nextInt();
		String query = "SELECT * FROM country WHERE id = " + id + ";";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"Country: " + rs.getString("name"));
		}
	}
	
	private static void showCityPerson() throws SQLException {
		System.out.println("Enter city");
		String nameCity = sc.next();

		String query = "SELECT * FROM city";
		int city_id = 0;
		boolean check = false;
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (nameCity.equals(rs.getString("name"))) {
				city_id = rs.getInt("id");
				check = true;
				break;
			} else
				check = false;
		}
		if(check) {
			query = "SELECT p.*, c.* FROM person p JOIN city c ON c.id = p.city_id WHERE c.id = " + city_id + ";";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"First Name: " + rs.getString("first_name") + "\t | " + 
					"Last Name: " + rs.getString("last_name") + "\t | " +
					"Age: " + rs.getInt("age") + "\t | " + 
					"City: " + rs.getString("name"));
			}
		} else System.out.println("City not found!");
	}
	
	private static void showCityCountry() throws SQLException {
		System.out.println("Enter country name");
		String nameCountry = sc.next();
		String query = "SELECT * FROM country;";
		int country_id = 0;
		boolean check = false;
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			if (nameCountry.equals(rs.getString("name"))) {
				country_id = rs.getInt("id");
				check = true;
				break;
			} else
				check = false;
		}
		if(check) {
			query = "SELECT c.*, co.name AS country_name FROM city c JOIN country co ON co.id = c.country_id WHERE co.id = " + country_id + ";";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"City: " + rs.getString("name") + "\t | " + 
					"Country: "+ rs.getString("country_name"));
			}
		} else System.out.println("Country not found!");
	}
	
	private static void showPersonForName() throws SQLException {
		System.out.println("Enter person name");
		String personName = sc.next();
		String query = "SELECT p.*, c.* FROM person p JOIN city c ON c.id = p.city_id WHERE first_name LIKE '%" + personName + "%';";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"First Name: " + rs.getString("first_name") + "\t | " + 
					"Last Name: " + rs.getString("last_name") + "\t | " +
					"Age: " + rs.getInt("age") + "\t | " + 
					"City: " + rs.getString("name"));
		}
	}
	
	private static void editPerson() throws SQLException {
		
		System.out.println("Enter person ID");
		int id = sc.nextInt();
		String query = "SELECT p.*, c.* FROM person p JOIN city c ON c.id = p.city_id WHERE p.id = " + id + ";";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			System.out.println(
					"ID: " + rs.getInt("id") + "\t | " + 
					"FirstName: " + rs.getString("first_name") + "\t | " + 
					"LastName: " + rs.getString("last_name") + "\t | " +
					"Age: " + rs.getInt("age") + "\t | " + 
					"City: " + rs.getString("name"));
		}
		System.out.println("What do you want to change?");
		String answer = sc.next();
		switch(answer) {
			case "FirstName": {
				System.out.println("Enter first name");
				String firstName = sc.next();
				query = "UPDATE person SET first_name = '" + firstName + "' WHERE id = " + id + ";";
				pstmt = conn.prepareStatement(query);
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println("Person successfully edited!");
				break;
			}
			case "LastName": {
				System.out.println("Enter last name");
				String lastName = sc.next();
				query = "UPDATE person SET last_name = '" + lastName + "' WHERE id = " + id + ";";
				pstmt = conn.prepareStatement(query);
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println("Person successfully edited!");
				break;
			}
			case "Age": {
				System.out.println("Enter age");
				int age = sc.nextInt();
				query = "UPDATE person SET age = '" + age + "' WHERE id = " + id + ";";
				pstmt = conn.prepareStatement(query);
				pstmt.executeUpdate();
				pstmt.close();
				System.out.println("Person successfully edited!");
				break;
			}
			case "City": {
				System.out.println("Enter city");
				String nameCity = sc.next();
				query = "SELECT * FROM city";
				int city_id = 0;
				boolean check = false;
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (nameCity.equals(rs.getString("name"))) {
						city_id = rs.getInt("id");
						check = true;
						break;
					} else
						check = false;
				}
				
				if(check) {
					query = "UPDATE person SET city_id = '" + city_id + "' WHERE id = " + id + ";";
					pstmt = conn.prepareStatement(query);
					pstmt.executeUpdate();
					pstmt.close();
					System.out.println("Person successfully edited!");
				}
				else System.out.println("City not found!");
				break;
			}
		}
	}
	
	
	private static void countryFile() throws SQLException {
		try(FileReader reader = new FileReader("country.txt"))
        {
            int c;
            while(((c=reader.read())!=-1)){
            	String name = "";
            	name = name + (char)c;
            	while(((c=reader.read())!=-1) && (c != 10)){
            		if(c == 13) continue;
            		name = name + (char)c;
                }
                String query = "INSERT INTO country(name) VALUES(?);";
        		PreparedStatement pstmt = conn.prepareStatement(query);
        		pstmt.setString(1, name);
        		pstmt.executeUpdate();
        		pstmt.close();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }   
	}
	
	private static void cityFile() throws SQLException {
		Random rd = new Random();
		try(FileReader reader = new FileReader("city.txt"))
        {
            int c;
            int country_id = 0;
            String name;
            while(((c=reader.read())!=-1)){
            	name = "";
            	country_id = rd.nextInt(20) + 1;
            	name = name + (char)c;
            	while(((c=reader.read())!=-1) && (c != 10)){
            		if(c == 13) continue;
            		name = name + (char)c;
                }
                String query = "INSERT INTO city(name, country_id) VALUES(?, ?);";
        		PreparedStatement pstmt = conn.prepareStatement(query);
        		pstmt.setString(1, name);
        		pstmt.setInt(2, country_id);
        		pstmt.executeUpdate();
        		pstmt.close();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }   
	}
	
	private static void personFile() throws SQLException {
		Random rd = new Random();
		String first_name = "";
		String last_name = "";
		String ageb = "";
		int age = 0;
		int city_id = 0;
		try(FileReader reader = new FileReader("person.txt"))
        {
            int c;
            while(((c=reader.read())!=-1)){
            	first_name = "";
            	last_name = "";
            	age = 0;
            	ageb = "";
            	city_id = rd.nextInt(39) + 1;
            	while(((c=reader.read())!=-1) && (c != 32)){
            		first_name = first_name + (char)c;
                }
                while(((c=reader.read())!=-1) && (c != 32)){
                    last_name = last_name + (char)c;
                }
                while(((c=reader.read())!=-1) && (c != 32)){
                	if(c == 13 || c == 10) break;
                	ageb = ageb + (char)c;
                }
                age = Integer.valueOf(ageb);
                
                String query = "INSERT INTO person(first_name, last_name, age, city_id) VALUES(?, ?, ?, ?);";
        		PreparedStatement pstmt = conn.prepareStatement(query);
        		pstmt.setString(1, first_name);
        		pstmt.setString(2, last_name);
        		pstmt.setInt(3, age);
        		pstmt.setInt(4, city_id);
        		pstmt.executeUpdate();
        		pstmt.close();
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }   
	}

}
