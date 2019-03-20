package ir.zabetan.ewallet.service.dto;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyDTO {
    private Currency currency;
    private BigDecimal price;


    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MoneyDTO{" +
                "currency=" + currency +
                ", price=" + price +
                '}';
    }
}
