package com.artclod.h2

import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
import java.util.UUID
import scala.slick.driver.H2Driver.simple._ // Use H2Driver to connect to an H2 database

object WorkingData {
	val workingDataURL = "jdbc:h2:mem:working_data_" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1"

	def loadCSVColumnsAllString(csvFile: String, tableName: String) = {
		Database.forURL(workingDataURL, driver = "org.h2.Driver") withSession {
			Q.updateNA("CREATE TABLE " + tableName + " AS SELECT * FROM CSVREAD('" + csvFile + "');").execute
		}
	}

	def run[T](code : => T) = {
		Database.forURL(workingDataURL, driver = "org.h2.Driver") withSession { code }
	}
}