package com.artclod.h2

import org.relique.jdbc.csv.CsvReader
import java.io.File

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
// Use the implicit threadLocalSession
import Database.threadLocalSession

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import java.util.UUID

object SimpleExample {

	def main2(args: Array[String]) {
		//		val reader = new CsvReader(new File("data/data.csv"));
	}

	def main(args: Array[String]) {

		H2CSVReader.loadCSV("data/data.csv")

//		val connection = DriverManager.getConnection(H2CSVReader.workingData, "sa", "");
//		val statement = connection.createStatement();
//		val res = statement.executeQuery("SELECT * FROM TEST")
//		System.err.println("foo");
//		System.err.println(res.getMetaData())
//		System.err.println(res.getMetaData().getColumnName(1))
//		System.err.println(res.getMetaData().getColumnName(2))
//		System.err.println(res.getMetaData().getColumnName(3))
//		System.err.println(res.getMetaData().getColumnClassName(1))
//		System.err.println(res.getMetaData().getColumnClassName(2))
//		System.err.println(res.getMetaData().getColumnClassName(3))

		Database.forURL(H2CSVReader.workingData, driver = "org.h2.Driver") withSession {
			Query(SimpleData) foreach { case (col1, col2, col3) => 
				println(" " + col1 + "\t" + col2 + "\t" + col3)
			}
		}
	}

}