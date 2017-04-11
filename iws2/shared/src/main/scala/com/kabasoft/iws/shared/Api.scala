package com.kabasoft.iws.shared


trait Api {
  //def motd(name: String): String
  //type A
  //def api:DAOApi[A]
  def welcome(name: String): String
  def create(item:IWS):Int
  def insert  (model:IWS):Int
  def all (item:IWS):List[IWS]
  def find (item:IWS) :List[IWS]
  def findSome (item:IWS):List[IWS]
  def update (item:IWS):List[IWS]
  def delete(item:IWS): List[IWS]

}
trait DAO [A]  {
  def create(modelId:Int) :Int
  def insert (model:List[A]):Int
  def update (item:A):Int
  def delete(item:A):Int
  def all(model:A):List[A]
  def find (item:A):List[A]
  def findSome(item:A):List[A]
  //def findSome1(id:Long):List[A]
}
