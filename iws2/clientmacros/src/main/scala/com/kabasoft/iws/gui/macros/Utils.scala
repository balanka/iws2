package com.kabasoft.iws.gui.macros

import java.time.LocalDate
import java.util.Date


object Utils{

  def format(d:Date) = {
    val START=1900
    val  x=LocalDate.of(d.getYear,d.getMonth, d.getDate)
    x.getDayOfMonth.toString.concat( ".".concat(x.getMonthValue.toString.concat(".").concat((x.getYear()+START).toString)))
  }

}
