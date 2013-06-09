package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ColumnTypeInfererSpec extends FlatSpec with ShouldMatchers {

	"inferedColumnType " should " return the first ColumnType if no data has been added" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnString)

		inferer.inferedColumnType.columnType should equal(ColumnInt)
	}

	"inferedColumnType " should " return the first first matching column type if data has been added" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnDouble, ColumnString)

		inferer.sample(Option("34.6"))

		inferer.inferedColumnType.columnType should equal(ColumnDouble)
	}

	"inferedColumnType " should " return the first first column type that matches all the data added" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnDouble, ColumnString)

		inferer.sample(Option("10"))
		inferer.sample(Option("34.6"))

		inferer.inferedColumnType.columnType should equal(ColumnDouble)
	}

	"inferedColumnType " should " throw if no column types match" in {
		val inferer = ColumnTypeInferer("test", ColumnInt)

		inferer.sample(Option("this is not an int"))

		intercept[IllegalStateException] {
			inferer.inferedColumnType
		}
	}

	"inferedColumnType " should " return cannot be null if no data has been added" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnString)

		inferer.inferedColumnType.canBeNull should equal(false)
	}

	"inferedColumnType " should " return the column can be null if any entry was empty" in {
		val inferer = ColumnTypeInferer("test", ColumnString)

		inferer.sample(Option("foo"))
		inferer.sample(Option("bar"))
		inferer.sample(None)

		inferer.inferedColumnType.canBeNull should equal(true)
	}

	"inferedColumnType " should " return the column can not be null if every entry had a value" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnString)

		inferer.sample(Option("foo"))
		inferer.sample(Option("bar"))
		inferer.sample(Option("baz"))

		inferer.inferedColumnType.canBeNull should equal(false)
	}

		"inferedColumnType " should " return a maxLength of 0 if nothing as been added" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnString)

		inferer.inferedColumnType.maxLength should equal(0)
	}
	
	"inferedColumnType " should " return a maxLength equal to the maximum size of the entries" in {
		val inferer = ColumnTypeInferer("test", ColumnInt, ColumnString)

		inferer.sample(Option("foo"))
		inferer.sample(None)
		inferer.sample(Option("bar____"))
		inferer.sample(Option("baz"))

		inferer.inferedColumnType.maxLength should equal(7)
	}
}