import java.util.Scanner;

/**
 * The menu you interact with. Run this file to use the bank.
 *
 * @author Thomas Sanchez
 */
public class BankApp {

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Bank bank = new Bank();
        boolean running = true;

        System.out.println("=== Bank Management System ===");

        while (running) {
            System.out.println("\n1. Open an account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer between accounts");
            System.out.println("5. Show all accounts");
            System.out.println("6. Show transaction history");
            System.out.println("7. Bank summary");
            System.out.println("8. Run month end (interest and fees)");
            System.out.println("0. Quit");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    openAccount(bank);
                    break;
                case 2:
                    deposit(bank);
                    break;
                case 3:
                    withdraw(bank);
                    break;
                case 4:
                    transfer(bank);
                    break;
                case 5:
                    showAccounts(bank);
                    break;
                case 6:
                    showHistory(bank);
                    break;
                case 7:
                    showSummary(bank);
                    break;
                case 8:
                    bank.runMonthEnd();
                    System.out.println("Month end complete. Interest and fees applied.");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("That's not an option. Try again.");
            }
        }

        System.out.println("Goodbye!");
    }

    private static void openAccount(Bank bank) {
        String name = readLine("Owner's name: ");
        String type = readLine("Account type (checking/savings/business): ");
        String pin = readLine("Create a 4-digit PIN: ");
        while (!pin.matches("\\d{4}")) {
            pin = readLine("The PIN must be exactly 4 digits. Try again: ");
        }
        Account account = bank.openAccount(name, type, pin);
        System.out.println("Opened " + account.getAccountType()
                + " account #" + account.getAccountNumber() + " for " + name + ".");
    }

    private static void deposit(Bank bank) {
        Account account = login(bank);
        if (account == null) {
            return;
        }
        double amount = readDouble("Amount to deposit: $");
        if (account.deposit(amount)) {
            System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, account.getBalance());
        } else {
            System.out.println("Deposit failed. The amount must be more than $0.");
        }
    }

    private static void withdraw(Bank bank) {
        Account account = login(bank);
        if (account == null) {
            return;
        }
        double amount = readDouble("Amount to withdraw: $");
        if (!confirmIfSuspicious(account, amount)) {
            return;
        }
        if (account.withdraw(amount)) {
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, account.getBalance());
        } else {
            System.out.println("Withdrawal failed. Check the amount and your balance.");
        }
    }

    private static void transfer(Bank bank) {
        System.out.println("Transfer FROM:");
        Account from = login(bank);
        if (from == null) {
            return;
        }
        int toNumber = readInt("Transfer TO account number: ");
        Account to = bank.findAccount(toNumber);
        if (to == null) {
            System.out.println("No account with that number.");
            return;
        }
        double amount = readDouble("Amount to transfer: $");
        if (!confirmIfSuspicious(from, amount)) {
            return;
        }
        if (from.transferTo(to, amount)) {
            System.out.printf("Moved $%.2f from #%d to #%d.%n",
                    amount, from.getAccountNumber(), to.getAccountNumber());
        } else {
            System.out.println("Transfer failed. Check the amount, your balance, and the account numbers.");
        }
    }

    private static boolean confirmIfSuspicious(Account account, double amount) {
        if (amount > 0 && amount <= account.getBalance() && account.isSuspicious(amount)) {
            System.out.printf("WARNING: This is more than %.0f%% of the balance and was flagged as possible fraud.%n",
                    Account.SUSPICIOUS_PERCENT * 100);
            String answer = readLine("Is this really you? (y/n): ");
            if (!answer.equalsIgnoreCase("y")) {
                System.out.println("Cancelled for your protection.");
                return false;
            }
        }
        return true;
    }

    private static void showAccounts(Bank bank) {
        if (bank.getAccounts().isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        for (Account account : bank.getAccounts()) {
            System.out.println(account);
        }
    }

    private static void showHistory(Bank bank) {
        Account account = login(bank);
        if (account == null) {
            return;
        }
        if (account.getHistory().isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        System.out.println("History for account #" + account.getAccountNumber() + ":");
        for (String entry : account.getHistory()) {
            System.out.println("  " + entry);
        }
    }

    private static void showSummary(Bank bank) {
        if (bank.getAccounts().isEmpty()) {
            System.out.println("No accounts yet.");
            return;
        }
        Account largest = bank.getLargestAccount();
        System.out.println("=== Bank Summary ===");
        System.out.println("Accounts: " + bank.getAccounts().size());
        System.out.printf("Total money in the bank: $%.2f%n", bank.getTotalBalance());
        System.out.println("Largest account: " + largest);
        System.out.println("Flagged transactions: " + bank.getTotalFlagged());
    }

    private static Account login(Bank bank) {
        int number = readInt("Account number: ");
        Account account = bank.findAccount(number);
        if (account == null) {
            System.out.println("No account with that number.");
            return null;
        }
        if (account.isLocked()) {
            System.out.println("This account is locked after too many wrong PINs.");
            return null;
        }
        String pin = readLine("PIN: ");
        if (account.checkPin(pin)) {
            return account;
        }
        if (account.isLocked()) {
            System.out.println("Wrong PIN. The account is now locked.");
        } else {
            System.out.println("Wrong PIN. Tries left: " + account.getTriesLeft());
        }
        return null;
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, like 25 or 19.99.");
            }
        }
    }
}
