package com.kabasoft.iws.gui.macros

import diode.Action


/**
  * Created by batemady on 1/5/16.
  */
case class Refresh[A](item:A) extends Action
case class UpdateAll[A](todos: Seq[A]) extends Action
case class Update[A](item: A)  extends Action
case class Delete[A](item: A) extends Action
case class Insert[A](item: A) extends Action
case class FindAll[A](item:A) extends Action
object PrintAll extends Action