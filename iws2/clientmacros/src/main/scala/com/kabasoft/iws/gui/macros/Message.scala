package com.kabasoft.iws.gui.macros

/**
  * Created by batemady on 1/5/16.
  */
case class Refresh[A](item:A)
case class UpdateAll[A](todos: Seq[A])
case class Update[A](item: A)
case class Delete[A](item: A)
case class Insert[A](item: A)
case class FindAll[A](item:A)