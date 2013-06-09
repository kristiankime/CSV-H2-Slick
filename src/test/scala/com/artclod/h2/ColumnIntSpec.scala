package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ColumnIntSpec extends FlatSpec with ShouldMatchers {

	"isValidSQL " should " say true for positive ints (eg 1000)" in {
		ColumnInt.isValidSQL("1000") should equal(true)
	}

	"isValidSQL " should " say false for arbitrary strings (eg adsf)" in {
		ColumnInt.isValidSQL("adsf") should equal(false)
	}

	"isValidSQL " should " say false for date strings (eg 2013-1-5)" in {
		ColumnInt.isValidSQL("2013-1-5") should equal(false)
	}

	"isValidSQL " should " say true for negative ints (eg -100)" in {
		ColumnInt.isValidSQL("-100") should equal(true)
	}

	"isValidSQL " should " say false for decimals (eg 100.45)" in {
		ColumnInt.isValidSQL("100.45") should equal(false)
	}

	"isValidSQL " should " say true for numbers with commas (eg 1,000,000)" in {
		ColumnInt.isValidSQL("1,000,000") should equal(true)
	}

	"isValidSQL " should " say true for max int (i.e. 2,147,483,647)" in {
		ColumnInt.isValidSQL("2,147,483,647") should equal(true)
	}

	"isValidSQL " should " say false for above max int (i.e. 2,147,483,647 + 1)" in {
		ColumnInt.isValidSQL("2,147,483,648") should equal(false)
	}

	"isValidSQL " should " say true for min int (i.e. -2,147,483,648)" in {
		ColumnInt.isValidSQL("-2,147,483,648") should equal(true)
	}

	"isValidSQL " should " say false for below min int (i.e. -2,147,483,648 - 1)" in {
		ColumnInt.isValidSQL("-2,147,483,649") should equal(false)
	}

}