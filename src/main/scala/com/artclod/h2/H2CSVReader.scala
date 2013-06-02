package com.artclod.h2

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import java.util.UUID

object H2CSVReader {
	val workingData = "jdbc:h2:mem:working_data_" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1" 

	def loadCSVFiles(csvDirectory: String) = {
		Class.forName("org.relique.jdbc.csv.CsvDriver")
		val props = new Properties()
		props.put("columnTypes", "")
		// Create a connection. The first command line parameter is
		// the directory containing the .csv files.
		// A single connection is thread-safe for use by several threads.
		val connection = DriverManager.getConnection("jdbc:relique:csv:" + csvDirectory, props)
		val statement = connection.createStatement()
		connection
	}

	def loadCSV(csvFile: String) = {
//		Class.forName("org.h2.Driver")
//		val connection = DriverManager.getConnection(workingData, "sa", "");
//		// val conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
////		// add application code here
////		// conn.close();
//		val statement = connection.createStatement();
//		statement.execute("CREATE TABLE TEST AS SELECT * FROM CSVREAD('" + csvFile + "');");

		// CREATE TABLE TEST AS SELECT * FROM CSVREAD('test.csv');	
//		connection;
		
		Database.forURL(workingData, driver = "org.h2.Driver") withSession {
			Q.updateNA("CREATE TABLE TEST AS SELECT * FROM CSVREAD('" + csvFile + "');").execute
			
			val getTestResult = GetResult(r => r.nextString + " " + r.nextString + " " + r.nextString)
			val res = Q.queryNA[String]("SELECT * FROM TEST")(getTestResult) foreach { c => print(c)}
		}
	}

}