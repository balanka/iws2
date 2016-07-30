package com.kabasoft.iws.gui

object StringUtils {

  val Account_headers = Seq ("Id", "Name", "Description", "Date","Balance", "Group Id")
  val Article_headers = Seq ("Id", "Name", "Description", "Qtty. unit","Pck. unit", "Group","P. Price","Avg Price","Sales price")
  val Bank_headers = Seq ("BIC", "Name", "Description")
  val BankAccount_headers = Seq ("IBAN", "Name", "Description", "BIC","Debit", "Credit")
  val BusinessPartner_headers = Seq ("Id", "Name", "Street", "Zip","City", "State", "Account")
  val Stock_headers = Seq ("Id","Item id", "Quantity", "Min stock")
  val Company_headers = Seq ("Id", "Name", "Street", "Zip","City", "State", "B.Account","Purch. Cl. acc","Sales. Cl. acc" ,"Pmt. Cl. acc","Sett. Cl. acc","Vat.ID", "TaxCode"," Cur. Periode", "Next Periode")
  val Inv_Trans_Headers = Seq ("Id", "Oid", "Store", "Account")
  val Fin_Trans_Headers = Seq ("Id", "Oid", "Store", "Account")
  val LineInv_Trans_Headers = Seq ("Id", "Item.Id", "Price", "Quantity", "Unit", "Vat", "Duedate")
  val LineFin_Trans_Headers = Seq ("Id", "Account", "Debit/Credit", "OAccount", "Amount", "Duedate",  "Text")

}
