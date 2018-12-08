package co.com.ias.deved

import java.util.{Calendar, Date, UUID}

import co.com.ias.deved.common.Amount

import scala.util.{Success, Try}

object common {
  type Amount = BigDecimal

  def today: Date = Calendar.getInstance.getTime


}

case class Balance(amount: Amount = 0) {

}


//case class Account(no: UUID, balance: Balance = Balance())

trait Account {
  def no: UUID

  def balance: Balance
}

case class SavingsAccount(no: UUID, balance: Balance, openDate: Date = common.today, closeDate: Option[Date] = None) extends Account

case class CheckingAccount(no: UUID, balance: Balance, openDate: Date = common.today, closeDate: Option[Date] = None) extends Account

object Account {
  def createSavingsAccount(no: UUID, initialBalance: Balance): SavingsAccount = {
    SavingsAccount(no, initialBalance)
  }

  def closeAccount(account: Account): Account = {
    account match {
      case account: SavingsAccount => account.copy(closeDate = Some(common.today))
      case account: CheckingAccount => {
        account.copy(closeDate = Some(common.today))
      }
      case _ => throw new IllegalArgumentException
    }
  }
}

object AccountServices {
  def calculateRateAccount(): BigDecimal = ???
}


trait Repository[A, IdType] {
  def query(id: IdType): Try[A]

  def store(a: A): Try[A]
}

trait AccountRepository extends Repository[Account, UUID] {
  def query(id: UUID): Try[Account]

  def store(a: Account): Try[Account]
}

trait InMemoryAccountRepository extends AccountRepository {
  var values: Map[UUID, Account] = Map.empty

  override def query(id: UUID): Try[Account] = {
    Success(values(id))
  }

  override def store(a: Account): Try[Account] = {
    val r = values += ((a.no, a))
    Success(a)
  }
}


object System extends App {
  val balance = Balance
  val account = Account(UUID.randomUUID(), Balance(12))
}
