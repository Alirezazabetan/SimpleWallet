package ir.zabetan.ewallet.web.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ir.zabetan.ewallet.domain.BankAccount;
import ir.zabetan.ewallet.service.BankAccountService;
import ir.zabetan.ewallet.service.dto.CreateBankAccountReqDTO;
import ir.zabetan.ewallet.service.dto.GetBalanceRespDTO;
import ir.zabetan.ewallet.service.dto.RequestModifyDTO;
import ir.zabetan.ewallet.service.dto.TransferMoneyReqDTO;
import ir.zabetan.ewallet.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private final BankAccountService bankAccountService;

    public BankAccountResource(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @ApiOperation( httpMethod = "POST" ,value = "Create new BankAccount",response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/create")
    public ResponseEntity<BankAccount> createAccount(@Valid @RequestBody CreateBankAccountReqDTO createBankAccountReqDTO) throws Exception {
        log.info("REST request to save a bankAccount : {}", createBankAccountReqDTO);
            BankAccount result = bankAccountService.save(createBankAccountReqDTO);
            return ResponseEntity.created(new URI("/api/create/" + result.getPhoneNumber()))
                    .headers(new HttpHeaders())
                    .body(result);
    }

    @ApiOperation( httpMethod = "PUT" ,value = "Top Up the bankAccount",response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/top-up")
    public ResponseEntity<BankAccount> topUpAccount(@Valid @RequestBody RequestModifyDTO requestModifyDTO) throws URISyntaxException {
        log.info("REST request to topUp the bankAccount : {}", requestModifyDTO);
        BankAccount result = bankAccountService.deposit(requestModifyDTO);
        return ResponseEntity.created(new URI("/api/create/" + result.getPhoneNumber()))
                .headers(new HttpHeaders())
                .body(result);
    }

    @ApiOperation( httpMethod = "PUT" ,value = "Withdraw from the bankAccount",response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping("/withdraw")
    public ResponseEntity<BankAccount> withdraw(@Valid @RequestBody RequestModifyDTO RequestModifyDTO) throws Exception {
        log.info("REST request to withdraw from the bankAccount : {}", RequestModifyDTO);
        BankAccount result = bankAccountService.withdraw(RequestModifyDTO);
        return ResponseEntity.created(new URI("/api/withdraw/" + result.getPhoneNumber()))
                .headers(new HttpHeaders())
                .body(result);
    }

    @ApiOperation( httpMethod = "GET" ,value = "Get balance from the bankAccount",response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/get-balance/{phoneNumber}")
    public ResponseEntity<GetBalanceRespDTO> getBalance(@Valid @Pattern(regexp = "^(01)[0-46-9]-*[0-9]{7,8}$" , message = "Your phoneNumber must be like this '0122233344'") @PathVariable String phoneNumber) throws Exception {
        log.info("REST request to get balance from the bankAccount : {}", phoneNumber);
        Double result = bankAccountService.getBalance(phoneNumber);
        return ResponseEntity.created(new URI("/api/withdraw/" + result))
                .headers(new HttpHeaders())
                .body(new GetBalanceRespDTO(result));
    }

    @ApiOperation( httpMethod = "PUT" ,value = "Transfer money between two bankAccounts",response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @PutMapping("/transfer-money")
    public ResponseEntity<String> transferMoney(@Valid @RequestBody TransferMoneyReqDTO transferMoneyReqDTO) throws Exception {
        log.info("REST request to transferMoney between two bankAccounts : {}", transferMoneyReqDTO);
        if ( transferMoneyReqDTO.getSenderPhoneNumber() == null) {
            throw new BadRequestException("Please specify the payer");
        }
        if (transferMoneyReqDTO.getReciverPhoneNumber()== null) {
            throw new BadRequestException("Please specify the payee");
        }
        bankAccountService.transferMoney(transferMoneyReqDTO);
        return new ResponseEntity<>("Sucess", HttpStatus.OK);
    }
}
