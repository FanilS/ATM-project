package barska.example;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ATM {

	private TreeMap<Integer, Integer> stock = new TreeMap<Integer, Integer>();
	// key = type of banknote, value = number of banknotes on stock

	private int totalAvailable;
	private int[] possibleValues = { 5, 10, 20, 50, 100, 200, 500 };

	public void replenishStock(int numberOfBanknotes) {
		for (int i = 0; i < possibleValues.length; i++) {
			stock.put(possibleValues[i], numberOfBanknotes);
			totalAvailable += possibleValues[i] * numberOfBanknotes;
		}
		System.out.println(String.format("Stock replenished and contains %,d UAH", totalAvailable));
		System.out.println(numberOfBanknotes + " banknotes of " + stock.keySet() + " UAH");
		System.out.println("==============================");
	}

	public void getBalance() {
		totalAvailable = 0;
		StringBuilder balance = new StringBuilder("Number of banknotes left:");
		for (Integer key : stock.keySet()) {
			balance.append("\n").append(key).append(" UAH banknotes:").append("\t").append(stock.get(key))
					.append(" available");
			totalAvailable += (key * stock.get(key));
		}
		System.out.println(balance);
		System.out.println(String.format("Total available:\t%,d UAH", totalAvailable));
		System.out.println("==============================");
	}

	public void withdrawMoney(int desiredAmount, int transactionId) {

		TreeSet<Integer> keySet = new TreeSet<Integer>(stock.keySet());

		// checking minimum available banknote value
		Iterator<Integer> iterator = keySet.iterator();
		int banknoteValue = 0;
		if (iterator.hasNext()) {
			do {
				banknoteValue = iterator.next();
			} while (stock.get(banknoteValue) == 0);
		} else {
			System.out.println("Transaction " + transactionId + ": cash stock is empty");
			return;
		}

		// checking if the desired amount is within the allowed range
		if (desiredAmount < possibleValues[0]) {
			System.out.println("Transaction " + transactionId);
			System.out.println("You are trying to withdraw " + desiredAmount + " UAH");
			System.out.println("Minimum amount is " + possibleValues[0]);
			System.out.println("==============================");
			return;
		}

		// checking if the desired amount is available
		if (desiredAmount > totalAvailable) {
			System.out.println("Transaction " + transactionId + ": ATM doesn't have enough cash");
			System.out.println("You are trying to withdraw " + desiredAmount + " UAH");
			System.out.println("Maximum you can withdraw is " + totalAvailable + " UAH");
			System.out.println("==============================");
			return;
		}

		// checking if the amount can be handled with available banknotes
		if (desiredAmount % banknoteValue != 0) {
			System.out.println("Transaction " + transactionId + ": invalid amount");
			System.out.println("You are trying to withdraw " + desiredAmount + " UAH");
			int option = desiredAmount - desiredAmount % banknoteValue;
			if (desiredAmount / banknoteValue >= 1) {
				System.out.println("You can withdraw " + option + " or " + (option + banknoteValue) + " UAH");
			} else {
				System.out.println("You can withdraw " + (option + banknoteValue) + " UAH");
			}
			System.out.println("==============================");
			return;
		}

		// after all pre-checking options, handling cash to client
		System.out.println("Transaction " + transactionId + ": " + desiredAmount + " UAH withdrawn");
		iterator = keySet.descendingIterator();
		while (desiredAmount > 0) {
			banknoteValue = iterator.next();
			int banknoteNumberNeeded = 0;
			if (desiredAmount >= banknoteValue) {
				banknoteNumberNeeded = desiredAmount / banknoteValue;
				if (stock.get(banknoteValue) - banknoteNumberNeeded >= 0) {
					stock.put(banknoteValue, stock.get(banknoteValue) - banknoteNumberNeeded);
					desiredAmount -= banknoteValue * banknoteNumberNeeded;
				} else {
					int banknoteNumberAvailable = stock.get(banknoteValue);
					stock.put(banknoteValue, 0);
					desiredAmount -= banknoteValue * banknoteNumberAvailable;
				}
			}
		}
		getBalance();
	}
}
