package ir.zabetan.ewallet.service.dto;


import javax.validation.constraints.*;
import java.math.BigDecimal;

public class RequestModifyDTO {

    @Pattern(regexp = "^(01)[0-46-9]-*[0-9]{7,8}$" , message = "Your phoneNumber must be like this '0122233344'")
    @NotNull(message = "the phoneNumber must not be null")
    @NotBlank(message = "the phoneNumber must not be blank")
    @NotEmpty(message = "the phoneNumber must not be empty")
    private String phoneNumber;

    @Min(value = 1,message = "The value should be greater than 0")
    private BigDecimal balance;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setBalance(long balance) {
        this.balance = new BigDecimal(balance);
    }

    @Override
    public String toString() {
        return "RequestModifyDTO{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
