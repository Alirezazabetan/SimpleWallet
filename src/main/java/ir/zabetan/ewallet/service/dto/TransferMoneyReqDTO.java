package ir.zabetan.ewallet.service.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class TransferMoneyReqDTO {

    @Pattern(regexp = "^(01)[0-46-9]-*[0-9]{7,8}$" , message = "Your phoneNumber must be like this '0122233344'")
    @NotNull(message = "the phoneNumber must not be null")
    @NotBlank(message = "the phoneNumber must not be blank")
    @NotEmpty(message = "the phoneNumber must not be empty")
    private String senderPhoneNumber;

    @Pattern(regexp = "^(01)[0-46-9]-*[0-9]{7,8}$" , message = "Your phoneNumber must be like this '0122233344'")
    @NotNull(message = "the phoneNumber must not be null")
    @NotBlank(message = "the phoneNumber must not be blank")
    @NotEmpty(message = "the phoneNumber must not be empty")
    private String reciverPhoneNumber;

    @NotNull
    @Min(value = 1,message = "The value should be greater than 0")
    private BigDecimal amount;

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getReciverPhoneNumber() {
        return reciverPhoneNumber;
    }

    public void setReciverPhoneNumber(String reciverPhoneNumber) {
        this.reciverPhoneNumber = reciverPhoneNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
