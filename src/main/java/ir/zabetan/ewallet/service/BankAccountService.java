package ir.zabetan.ewallet.service;

import ir.zabetan.ewallet.domain.BankAccount;
import ir.zabetan.ewallet.repository.BankAccountRepository;
import ir.zabetan.ewallet.service.dto.CreateBankAccountReqDTO;
import ir.zabetan.ewallet.service.dto.RequestModifyDTO;
import ir.zabetan.ewallet.service.dto.TransferMoneyReqDTO;
import ir.zabetan.ewallet.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;


@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    private final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public void checkThreshold(RequestModifyDTO requestModifyDTO, BankAccount bankAccount) throws Exception {
        if (!bankAccount.getThresholdDate().equals(LocalDate.now())) {
            bankAccount.setThresholdDate(LocalDate.now());
            bankAccount.setThreshold(2000000d);
            update(bankAccount);
        }
        if ((bankAccount.getThreshold() - requestModifyDTO.getBalance().doubleValue()) < 0) {
            throw new Exception("You are not allowed to transfer this amount of money");
        }
    }

    private void checkBalance(RequestModifyDTO requestModifyDTO, BankAccount bankAccount) {
        if (!(bankAccount.getBalance().doubleValue() >= requestModifyDTO.getBalance().doubleValue())) {
            throw new BadRequestException("The balance is lower than the limit");
        }
    }

    @Transactional
    public BankAccount save(CreateBankAccountReqDTO createBankAccountReqDTO) {
        if (bankAccountRepository.findBankAccountByPhoneNumber(createBankAccountReqDTO.getPhoneNumber()).isPresent()){
            log.debug("The bankAccount is exist");
            throw new BadRequestException("The bankAccount is exist");
        }
        BankAccount account = new BankAccount();
        account.setBalance(createBankAccountReqDTO.getBalance());
        account.setPhoneNumber(createBankAccountReqDTO.getPhoneNumber());
        return bankAccountRepository.save(account);
    }

    private BankAccount findOne(String phoneNumber) {
        Optional<BankAccount> result = bankAccountRepository.findBankAccountByPhoneNumber(phoneNumber);
        if (!result.isPresent()){
            log.debug("The bankAccount does not exist");
            throw new BadRequestException("The bankAccount does not exist");
        }
        return result.get();
    }

    @Transactional
    public BankAccount deposit(RequestModifyDTO requestModifyDTO) {
        log.info("Request to deposit inventory", requestModifyDTO);
        BankAccount result = findOne(requestModifyDTO.getPhoneNumber());
        result.setBalance(result.getBalance().add(requestModifyDTO.getBalance()));
        return update(result);
    }

    @Transactional
    public BankAccount withdraw(RequestModifyDTO requestModifyDTO) throws Exception {
        log.info("Request to withdraw inventory", requestModifyDTO);
        BankAccount bankAccount = findOne(requestModifyDTO.getPhoneNumber());
        checkThreshold(requestModifyDTO, bankAccount);
        checkBalance(requestModifyDTO, bankAccount);
        bankAccount.setBalance(bankAccount.getBalance().subtract(requestModifyDTO.getBalance()));
        return update(bankAccount);
    }

    public double getBalance(String phoneNumber) {
        BankAccount bankAccount = findOne(phoneNumber);
        return bankAccount.getBalance().doubleValue();
    }

    @Transactional
    public void transferMoney(TransferMoneyReqDTO transferMoneyReqDTO) throws Exception {
        if (transferMoneyReqDTO.getReciverPhoneNumber().equals(transferMoneyReqDTO.getSenderPhoneNumber())){
            throw new BadRequestException("The payer and payee are same");
        }
        RequestModifyDTO requestDecrease = new RequestModifyDTO();
        requestDecrease.setBalance(transferMoneyReqDTO.getAmount());
        requestDecrease.setPhoneNumber(transferMoneyReqDTO.getSenderPhoneNumber());
        withdraw(requestDecrease);

        RequestModifyDTO requestIncrease = new RequestModifyDTO();
        requestIncrease.setBalance(transferMoneyReqDTO.getAmount());
        requestIncrease.setPhoneNumber(transferMoneyReqDTO.getReciverPhoneNumber());
        deposit(requestIncrease);
    }

    @Transactional
    public BankAccount update(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void delete(BankAccount result) {
        bankAccountRepository.delete(result);
    }

}
