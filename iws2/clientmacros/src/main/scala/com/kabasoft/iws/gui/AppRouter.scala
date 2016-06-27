package com.kabasoft.iws.gui

object AppRouter {

  sealed trait Page
  case object Home extends Page
  case object DashboardPage$ extends Page
  case object TodoPage$ extends Page
  case object CostCenterPage$ extends Page
  case object AccountPage$ extends Page
  case object ArticlePage$ extends Page
  case object QuantityUnitPage$ extends Page
  case object CategoryPage$ extends Page
  case object POrderPage$ extends Page
  case object CustomerPage$ extends Page
  case object VatPage$ extends Page

  case object SupplierPage$ extends Page
  case object ScalaCSSDoc extends Page
  case object Components2Doc  extends Page
  case object DropDownDoc  extends Page



}
