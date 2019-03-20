package ir.zabetan.ewallet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ir.zabetan.ewallet.domain.BankAccount;
import ir.zabetan.ewallet.repository.BankAccountRepository;
import ir.zabetan.ewallet.service.BankAccountService;
import ir.zabetan.ewallet.service.dto.RequestModifyDTO;
import ir.zabetan.ewallet.web.rest.BankAccountResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankAccountRestApplicationTests {

    private MockMvc restAccountMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountResource bankAccountResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.restAccountMockMvc = MockMvcBuilders.standaloneSetup(bankAccountResource)
                .setMessageConverters(jacksonMessageConverter).build();
        this.bankAccount = createEntity();
    }

    private BankAccount bankAccount;

    public BankAccount createEntity() {
        return new BankAccount()
                .phoneNumber("09123334455")
                .balance(88888);
    }

    @Test
    public void createAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        restAccountMockMvc.perform(post("/api/create")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(bankAccount)))
                .andExpect(status().isCreated());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount bankAccountTest = bankAccounts.get(bankAccounts.size() - 1);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("09123334455");
        assertThat(bankAccountTest.getBalance().longValue()).isEqualTo(88888L);

        bankAccountRepository.delete(bankAccount);
    }

    @Test
    public void increaseRuleGroup() throws Exception {
        bankAccountRepository.save(bankAccount);
        RequestModifyDTO requestIncreaseDTO = new RequestModifyDTO();
        requestIncreaseDTO.setPhoneNumber(bankAccount.getPhoneNumber());
        requestIncreaseDTO.setBalance(11111);

        restAccountMockMvc.perform(post("/api/deposit")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(requestIncreaseDTO)))
                .andExpect(status().isCreated());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
//		assertThat(bankAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount bankAccountTest = bankAccounts.get(0);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("09123334455");
        assertThat(bankAccountTest.getBalance().longValue()).isEqualTo(99999L);

        bankAccountRepository.delete(bankAccount);
    }


    @Test
    public void decreaseRuleGroup() throws Exception {
        bankAccountRepository.save(bankAccount);
        RequestModifyDTO requestModifyDTO = new RequestModifyDTO();
        requestModifyDTO.setPhoneNumber(bankAccount.getPhoneNumber());
        requestModifyDTO.setBalance(11111);

        restAccountMockMvc.perform(post("/api/withdraw")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(requestModifyDTO)))
                .andExpect(status().is(201));

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
//		assertThat(bankAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount bankAccountTest = bankAccounts.get(0);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("09123334455");
        assertThat(bankAccountTest.getBalance().longValue()).isEqualTo(77777L);

        bankAccountRepository.delete(bankAccount);
    }

    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

}
