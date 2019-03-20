package ir.zabetan.ewallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account")
public class BankAccount {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long accountNum;

    @Column(unique=true)
    private String phoneNumber;

    private BigDecimal balance;

    @JsonIgnore
    private Double threshold = 2000000d;
    @JsonIgnore
    private LocalDate thresholdDate = LocalDate.now();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BankAccount phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BankAccount balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public BankAccount balance(long balance) {
        this.balance = new BigDecimal(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBalance(long balance) {
        this.balance = new BigDecimal(balance);
    }

    public long getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(long accountNum) {
        this.accountNum = accountNum;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public LocalDate getThresholdDate() {
        return thresholdDate;
    }

    public void setThresholdDate(LocalDate thresholdDate) {
        this.thresholdDate = thresholdDate;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNum=" + accountNum +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance=" + balance +
                ", threshold=" + threshold +
                ", thresholdDate=" + thresholdDate +
                '}';
    }
}
