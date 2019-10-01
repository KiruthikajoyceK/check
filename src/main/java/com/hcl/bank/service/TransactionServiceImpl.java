package com.hcl.bank.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bank.constants.FundtransferConstants;
import com.hcl.bank.dto.FundTransferRequestDto;
import com.hcl.bank.dto.FundTransferResponseDto;
import com.hcl.bank.dto.TransactionResponseDto;
import com.hcl.bank.entity.Account;
import com.hcl.bank.entity.Benificiary;
import com.hcl.bank.entity.Transaction;
import com.hcl.bank.exception.CommonException;
import com.hcl.bank.repository.AccountRepository;
import com.hcl.bank.repository.BenificiaryRepository;
import com.hcl.bank.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionServiceIntf {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	BenificiaryRepository benificiaryRepository;

	@Override
	public FundTransferResponseDto fundTransfer(FundTransferRequestDto fundTransferRequestDto) {

		double transactionAmount = fundTransferRequestDto.getTransactionAmount();

		if (fundTransferRequestDto.getFromAccountNo() == fundTransferRequestDto.getToAccountNo()) {
			throw new CommonException(FundtransferConstants.ACCOUNT_SAME);
		}
		Account fromAccount = accountRepository.findByAccountNumber(fundTransferRequestDto.getFromAccountNo());
        
		
		  int userId=fromAccount.getUser().getUserId();
		  List<Benificiary> benificiary=benificiaryRepository.findBenificiaryByUserId(userId); 
		  
		  for(Benificiary benificiary2 : benificiary) {
			  
			  System.out.println(""+benificiary2.getBenificiaryAccountNo());
			
			  if(benificiary2.getBenificiaryAccountNo()!=fundTransferRequestDto.getToAccountNo())
			  { 
				  System.out.println("2"+benificiary2.getBenificiaryAccountNo());
				  throw new CommonException(FundtransferConstants.NOT_IN_LIST);
			  }
			  
		Account toAccount = accountRepository.findByAccountNumber(fundTransferRequestDto.getToAccountNo());
		if (fundTransferRequestDto.getTransactionAmount() == 0) {
			throw new CommonException(FundtransferConstants.INVALID_BALANCE);
		}
		if (fundTransferRequestDto.getTransactionAmount() < 0) {
			throw new CommonException(FundtransferConstants.INVALID_NEGATIVE_BALANCE);
		}
		if (fundTransferRequestDto.getTransactionAmount() > fromAccount.getAccountBalance()) {
			throw new CommonException(FundtransferConstants.INVALID_SUFFICIENT_BALANCE);
		}

		double userBalance = fromAccount.getAccountBalance();
		double benificiaryBalance = benificiary2.getBenificiaryAccountBalance();
		double debitBalance = userBalance - transactionAmount;
		double creditBalance = benificiaryBalance + transactionAmount;

		fromAccount.setAccountBalance(debitBalance);
		accountRepository.save(fromAccount);

		benificiary2.setBenificiaryAccountBalance(creditBalance);
		benificiaryRepository.save(benificiary2);

		Transaction debitTransaction = new Transaction();
		debitTransaction.setFromAccountNo(fundTransferRequestDto.getFromAccountNo());
		debitTransaction.setToAccountNo(fundTransferRequestDto.getToAccountNo());
		debitTransaction.setTransactionAmount(fundTransferRequestDto.getTransactionAmount());
		debitTransaction.setTransactionDate(LocalDate.now());
		debitTransaction.setTransactionType(FundtransferConstants.DEBITED);
		debitTransaction.setUser(fromAccount.getUser());
		transactionRepository.save(debitTransaction);

		Transaction creditTransaction = new Transaction();
		creditTransaction.setFromAccountNo(fundTransferRequestDto.getFromAccountNo());
		creditTransaction.setToAccountNo(fundTransferRequestDto.getToAccountNo());
		creditTransaction.setTransactionAmount(fundTransferRequestDto.getTransactionAmount());
		creditTransaction.setTransactionDate(LocalDate.now());
		creditTransaction.setTransactionType(FundtransferConstants.CREDITED);
		creditTransaction.setBenificiary(benificiary2);
		transactionRepository.save(creditTransaction);
		
		  }
		FundTransferResponseDto fundTransferResponseDto = new FundTransferResponseDto();
		fundTransferResponseDto.setMessage("transferred successfully");

		return fundTransferResponseDto;
	}
	
	
	

	public List<TransactionResponseDto> transactionHistory(int userId, int noOfWeeks, int noOfMonths) {

		LocalDate currentDate = LocalDate.now();

		List<TransactionResponseDto> transactionResponseDtos = new ArrayList<TransactionResponseDto>();

		if (noOfMonths == 0) {
			LocalDate calculatedDate = currentDate.minusWeeks(noOfWeeks);

			List<Transaction> transactions = transactionRepository.findByUserAndTransactionDate(userId, calculatedDate,
					currentDate);

			for (Transaction transaction : transactions) {
				TransactionResponseDto transactionResponseDto = new TransactionResponseDto();

				transactionResponseDto.setFromAccountNo(transaction.getFromAccountNo());
				transactionResponseDto.setToAccountNo(transaction.getToAccountNo());
				transactionResponseDto.setTransactionAmount(transaction.getTransactionAmount());
				transactionResponseDto.setTransactionDate(transaction.getTransactionDate());
				transactionResponseDto.setTransactionType(transaction.getTransactionType());
				transactionResponseDtos.add(transactionResponseDto);
			}

		}

		else if (noOfWeeks == 0) {
			LocalDate calculatedDate = currentDate.minusMonths(noOfMonths);

			List<Transaction> transactions = transactionRepository.findByUserAndTransactionDate(userId, calculatedDate,
					currentDate);

			for (Transaction transaction : transactions) {
				TransactionResponseDto transactionResponseDto = new TransactionResponseDto();

				transactionResponseDto.setFromAccountNo(transaction.getFromAccountNo());
				transactionResponseDto.setToAccountNo(transaction.getToAccountNo());
				transactionResponseDto.setTransactionAmount(transaction.getTransactionAmount());
				transactionResponseDto.setTransactionDate(transaction.getTransactionDate());
				transactionResponseDto.setTransactionType(transaction.getTransactionType());
				transactionResponseDtos.add(transactionResponseDto);

			}

		}

		return transactionResponseDtos;

	}

}
