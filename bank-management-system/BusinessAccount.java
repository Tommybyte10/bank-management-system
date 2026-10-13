/**
 * A business account. Pays a monthly service fee,
 * which is waived if the balance is high enough.
 *
 * @author Thomas Sanchez
 */
public class BusinessAccount extends Account {

    public static final double SERVICE_FEE = 15.0;
    public static final double FEE_WAIVER_BALANCE = 5000.0;

    public BusinessAccount(String ownerName, int accountNumber, String pin) {
        super(ownerName, accountNumber, pin);
    }

    @Override
    public String getAccountType() {
        return "Business";
    }

    @Override
    public void monthEnd() {
        if (balance < FEE_WAIVER_BALANCE && balance >= SERVICE_FEE) {
            balance = balance - SERVICE_FEE;
            addHistory("Business service fee", SERVICE_FEE);
        }
    }
}
