package com.kabasoft.iws.gui

trait Observer[S] {
  def update(subject: S);
}

trait Subject[S] {
  this: S =>
  private var observers: List[Observer[S]] = Nil
  def addObserver(observer: Observer[S]) = observers = observer :: observers
  def notifyObservers() = observers.foreach(_.update(this))
}

class Account(initialBalance: Double) {
  private var currentBalance = initialBalance
  def balance = currentBalance
  def deposit(amount: Double)  = currentBalance += amount
  def withdraw(amount: Double) = currentBalance -= amount
}

class ObservedAccount(initialBalance: Double) extends Account(initialBalance) with Subject[Account] {
  override def deposit(amount: Double) = {
    super.deposit(amount)
    notifyObservers()
  }
  override def withdraw(amount: Double) = {
    super.withdraw(amount)
    notifyObservers()
  }
}


class AccountReporter extends Observer[Account] {
  def update(account: Account) =
    println("Observed balance change: "+account.balance)
}