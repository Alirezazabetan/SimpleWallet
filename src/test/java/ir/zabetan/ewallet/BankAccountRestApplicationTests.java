package ir.zabetan.ewallet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ir.zabetan.ewallet.domain.BankAccount;
import ir.zabetan.ewallet.repository.BankAccountRepository;
import ir.zabetan.ewallet.service.dto.CreateBankAccountReqDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankAccountRestApplicationTests {

    private MockMvc restAccountMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;



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
                .phoneNumber("0123334455")
                .balance(88888);
    }

    public CreateBankAccountReqDTO createDTOEntity() {
        CreateBankAccountReqDTO acc = new CreateBankAccountReqDTO();
        acc.setPhoneNumber(bankAccount.getPhoneNumber());
        acc.setBalance(bankAccount.getBalance());
        return acc;
    }

    @Test
    public void createAccount() throws Exception {
        int databaseSizeBeforeCreate = bankAccountRepository.findAll().size();

        CreateBankAccountReqDTO acc = new CreateBankAccountReqDTO();
        acc.setPhoneNumber(bankAccount.getPhoneNumber());
        acc.setBalance(bankAccount.getBalance());
        restAccountMockMvc.perform(post("/api/create")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(acc)))
                .andExpect(status().isCreated());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        assertThat(bankAccounts).hasSize(databaseSizeBeforeCreate + 1);
        BankAccount bankAccountTest = bankAccounts.get(bankAccounts.size() - 1);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("0123334455");
        assertThat(bankAccountTest.getBalance().longValue()).isEqualTo(88888L);

        bankAccountRepository.deleteById(bankAccounts.get(0).getAccountNum());
    }

    @Test
    public void top_up() throws Exception {
        bankAccountRepository.save(bankAccount);
        RequestModifyDTO requestIncreaseDTO = new RequestModifyDTO();
        requestIncreaseDTO.setPhoneNumber(bankAccount.getPhoneNumber());
        requestIncreaseDTO.setBalance(11111);

        restAccountMockMvc.perform(put("/api/top-up")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(requestIncreaseDTO)))
                .andExpect(status().isCreated());

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        BankAccount bankAccountTest = bankAccounts.get(0);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("0123334455");
        assertThat(bankAccountTest.getBalance().longValue()).isEqualTo(99999L);

        bankAccountRepository.deleteById(bankAccounts.get(0).getAccountNum());
    }


    @Test
    public void withdraw() throws Exception {
        bankAccountRepository.save(bankAccount);
        RequestModifyDTO requestModifyDTO = new RequestModifyDTO();
        requestModifyDTO.setPhoneNumber(bankAccount.getPhoneNumber());
        requestModifyDTO.setBalance(11111);

        restAccountMockMvc.perform(put("/api/withdraw")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(requestModifyDTO)))
                .andExpect(status().is(201));

        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        BankAccount bankAccountTest = bankAccounts.get(0);
        assertThat(bankAccountTest.getPhoneNumber()).isEqualTo("0123334455");
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
