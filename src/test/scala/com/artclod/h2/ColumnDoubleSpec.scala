package com.artclod.h2

import collection.mutable.Stack
import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class ColumnDoubleSpec extends FlatSpec with ShouldMatchers {

	ColumnDouble + "'s isValidSQL " should " say true for positive ints (eg 1000)" in {
		ColumnDouble.isValidSQL("1000") should equal(true)
	}

	ColumnDouble + "'s isValidSQL " should " say false for arbitrary strings (eg adsf)" in {
		ColumnDouble.isValidSQL("adsf") should equal(false)
	}

	ColumnDouble + "'s isValidSQL " should " say false for date strings (eg 2013-1-5)" in {
		ColumnDouble.isValidSQL("2013-1-5") should equal(false)
	}
	
	ColumnDouble + "'s isValidSQL " should " say true for negative ints (eg -100)" in {
		ColumnDouble.isValidSQL("-100") should equal(true)
	}

	ColumnDouble + "'s isValidSQL " should " say true for decimals (eg 100.45)" in {
		ColumnDouble.isValidSQL("100.45") should equal(true)
	}
	
	ColumnDouble + "'s isValidSQL " should " say true for numbers with commas (eg 1,000,000)" in {
		ColumnDouble.isValidSQL("1,000,000") should equal(true)
	}

}