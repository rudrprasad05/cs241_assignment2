package com.group6.assignment2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Assignment2Application {


	public static void main(String[] args) {
		MySQLConnectionTest mySQLConnectionTest = new MySQLConnectionTest();
		if(mySQLConnectionTest.test()){
			SpringApplication.run(Assignment2Application.class, args);
		}
		else{
			System.out.println("Failed to connect to database. Check if mysql is running");
			System.exit(0);
		}

	}

}
