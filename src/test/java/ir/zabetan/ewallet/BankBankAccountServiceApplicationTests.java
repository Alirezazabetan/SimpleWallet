package ir.zabetan.ewallet;

import ir.zabetan.ewallet.domain.BankAccount;
import ir.zabetan.ewallet.service.BankAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BankBankAccountServiceApplicationTests {

	@Autowired
	private BankAccountService bankAccountService;

	private BankAccount bankAccount;

	public BankAccount createEntity(){
		return new BankAccount()
				.phoneNumber("09123334455")
				.balance(99999);
	}

	@Before
	public void run(){
		this.bankAccount = createEntity();
	}

//	@Test
//	public void save() {
//		BankAccount result = bankAccountService.save(this.bankAccount);
//		assertEquals(result.getPhoneNumber(), bankAccount.getPhoneNumber());
//		assertEquals(result.getBalance().longValue(), bankAccount.getBalance().longValue());
//
//		bankAccountService.delete(result);
//	}

	@Test
	public void find_one() {
//		accountService.save(this.bankAccount);
//
//		accountService.findOne(this.bankAccount.getPhoneNumber());
//		assertEquals(result.getPhoneNumber(),bankAccount.getPhoneNumber());
//		assertEquals(result.getBalance().longValue(),bankAccount.getBalance().longValue());
//
//		accountService.delete(result);
	}



}
