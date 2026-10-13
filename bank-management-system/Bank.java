import java.util.ArrayList;

/**
 * Holds every account, gives each new one a unique account number,
 * and reports totals for the whole bank.
 *
 * @author Thomas Sanchez
 */
public class Bank {

    private ArrayList<Account> accounts;
    private int nextAccountNumber;

    public Bank() {
        accounts = new ArrayList<Account>();
        nextAccountNumber = 1001;
    }

    public Account openAccount(String ownerName, String type, String pin) {
        Account account;
        if (type.equalsIgnoreCase("savings")) {
            account = new SavingsAccount(ownerName, nextAccountNumber, pin);
        } else if (type.equalsIgnoreCase("business")) {
            account = new BusinessAccount(ownerName, nextAccountNumber, pin);
        } else {
            account = new CheckingAccount(ownerName, nextAccountNumber, pin);
        }
        accounts.add(account);
        nextAccountNumber++;
        return account;
    }

    public Account findAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void runMonthEnd() {
        for (Account account : accounts) {
            account.monthEnd();
        }
    }

    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total = total + account.getBalance();
        }
        return total;
    }

    public Account getLargestAccount() {
        Account largest = null;
        for (Account account : accounts) {
            if (largest == null || account.getBalance() > largest.getBalance()) {
                largest = account;
            }
        }
        return largest;
    }

    public int getTotalFlagged() {
        int total = 0;
        for (Account account : accounts) {
            total = total + account.countFlagged();
        }
        return total;
    }
}
