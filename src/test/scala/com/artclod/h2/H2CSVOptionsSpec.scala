package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.slick.lifted.Query
import scala.slick.session.Database
import Database.threadLocalSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import scala.slick.session.PositionedResult


@RunWith(classOf[JUnitRunner])
class H2CSVOptionsSpec extends FlatSpec with ShouldMatchers {
	implicit private val stringTuple = GetResult(r => (r.nextString, r.nextString, r.nextString))
	
	"default setup " should " load comma separated data" in {
		WorkingData.loadCSVColumnsAllString("classpath:/com/artclod/h2/data-comma.csv", "COMMA_TEST")

		val values = WorkingData.run(Q.queryNA("SELECT * FROM \"COMMA_TEST\"").list)

		values should equal(("1", "a", "2013-1-5") :: 
				("2", "b", "2013-1-6") :: 
				("3", "c", "2013-1-7") :: Nil)
	}

}