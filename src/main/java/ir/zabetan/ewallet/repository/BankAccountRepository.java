package ir.zabetan.ewallet.repository;

import ir.zabetan.ewallet.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    Optional<BankAccount> findBankAccountByPhoneNumber(String phoneNumber);
}
