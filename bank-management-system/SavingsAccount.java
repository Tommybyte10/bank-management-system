/**
 * A savings account. Earns interest at the end of every month.
 *
 * @author Thomas Sanchez
 */
public class SavingsAccount extends Account {

    public static final double MONTHLY_INTEREST_RATE = 0.01;

    public SavingsAccount(String ownerName, int accountNumber, String pin) {
        super(ownerName, accountNumber, pin);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public void monthEnd() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        if (interest > 0) {
            balance = balance + interest;
            addHistory("Interest", interest);
        }
    }
}
