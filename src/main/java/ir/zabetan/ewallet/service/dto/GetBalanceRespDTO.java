package ir.zabetan.ewallet.service.dto;

public class GetBalanceRespDTO {
    private double balance;

    public GetBalanceRespDTO(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
