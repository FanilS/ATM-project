package barska.example;

public class Main {

	public static void main(String[] args) {

		ATM atm = new ATM();

		// adding random number of banknotes of all values into the stock
		int numberOfBanknotes = (int) (Math.random() * 45 + 5);
		atm.replenishStock(numberOfBanknotes);

		// 20 attempts to withdraw random amounts of money
		int transactionId = 0;
		while (transactionId < 20) {
			atm.withdrawMoney((int) (Math.random() * 10000) + 1, ++transactionId);
		}
	}
}
