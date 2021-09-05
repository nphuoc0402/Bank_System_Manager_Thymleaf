package com.cg.service.customer;

import com.cg.dto.DepositDTO;
import com.cg.dto.RecipientDTO;
import com.cg.dto.TransferDTO;
import com.cg.dto.WithdrawDTO;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DepositRepository;
import com.cg.repository.TransferRepository;
import com.cg.repository.WithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Override
    public Iterable<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Iterable<Customer> findAllByDeletedIsFalse() {
        return customerRepository.findAllByDeletedIsFalse();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void remove(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<DepositDTO> findByIdWithDepositDTO(Long id) {
        return customerRepository.findByIdWithDepositDTO(id);
    }

    @Override
    public Optional<WithdrawDTO> findByIdWithWithdrawDTO(Long id) {
        return customerRepository.findByIdWithWithdrawDTO(id);
    }

    @Override
    public Iterable<RecipientDTO> findAllRecipientDTOByIdWithOutSender(Long id) {
        return customerRepository.findAllRecipientDTOByIdWithOutSender(id);
    }

    @Override
    public Iterable<RecipientDTO> findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(Long id) {
        return customerRepository.findAllRecipientDTOByIdWithOutSenderAndDeletedIsFalse(id);
    }

    @Override
    public void doDeposit(Long customerId, BigDecimal transactionAmount, DepositDTO depositDTO) {
        customerRepository.incrementBalance(transactionAmount, customerId);

        Customer customer = new Customer();
        customer.setId(customerId);
        Deposit deposit = new Deposit();
        deposit.setCustomer(customer);
        deposit.setTransactionAmount(transactionAmount);
        depositRepository.save(deposit);
    }

    @Override
    public void doWithdraw(Long customerId, BigDecimal transactionAmount, WithdrawDTO withdrawDTO) {
        customerRepository.reduceBalance(transactionAmount, customerId);

        Customer customer = new Customer();
        customer.setId(customerId);
        Withdraw withdraw = new Withdraw();
        withdraw.setCustomer(customer);
        withdraw.setTransactionAmount(transactionAmount);
        withdrawRepository.save(withdraw);
    }

    @Override
    public void doTransfer(TransferDTO transferDTO) {
        customerRepository.reduceBalance(transferDTO.getTransactionAmount(), transferDTO.getSenderId());

        customerRepository.incrementBalance(transferDTO.getTransferAmount(), transferDTO.getRecipientId());

        Customer customer1 = new Customer();
        customer1.setId(transferDTO.getSenderId());
        Customer customer2 = new Customer();
        customer2.setId(transferDTO.getSenderId());
        Transfer transfer = new Transfer();
        transfer.setCustomerSend(customer1);
        transfer.setCustomerRec(customer2);
        transfer.setTransferAmount(transferDTO.getTransferAmount());
        transfer.setFees(transferDTO.getFees());
        transfer.setFeesAmount(transferDTO.getFeesAmount());
        transfer.setTransactionAmount(transferDTO.getTransactionAmount());
        transferRepository.save(transfer);
    }

    @Override
    public void incrementBalance(BigDecimal balance, Long id) {
        customerRepository.incrementBalance(balance, id);
    }

    @Override
    public void reduceBalance(BigDecimal balance, Long id) {
        customerRepository.reduceBalance(balance, id);
    }
}
