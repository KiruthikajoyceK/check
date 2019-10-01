package com.hcl.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.bank.constants.FundtransferConstants;
import com.hcl.bank.dto.BenificiaryRequestDto;
import com.hcl.bank.dto.BenificiaryResponseDto;
import com.hcl.bank.entity.Account;
import com.hcl.bank.entity.Benificiary;
import com.hcl.bank.entity.User;
import com.hcl.bank.exception.CommonException;
import com.hcl.bank.repository.AccountRepository;
import com.hcl.bank.repository.BenificiaryRepository;
import com.hcl.bank.repository.UserRepository;

@Service
public class BenificiaryServiceImpl implements BenificiaryServiceIntf {

	@Autowired
	BenificiaryRepository benificiaryRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AccountRepository  accountRepository;

	@Override
	public BenificiaryResponseDto addBenificiary(BenificiaryRequestDto benificiaryRequestDto) {

		int userId = benificiaryRequestDto.getUserId();
		User user = userRepository.findByUserId(userId);

		List<Benificiary> benificiaries = benificiaryRepository.findBenificiaryByUserId(userId);
		
		if(benificiaries.isEmpty())
		{
			Benificiary benificiary = new Benificiary();
			benificiary.setBenificiaryAccountNo(benificiaryRequestDto.getBenificiaryAccountNo());

			if (benificiaryRequestDto.getBenificiaryAccountType().isEmpty()) {
				throw new CommonException(FundtransferConstants.BENIFICIARY_ACCOUNT_TYPE);
			}
			benificiary.setBenificiaryAccountType(benificiaryRequestDto.getBenificiaryAccountType());

			if (benificiaryRequestDto.getBenificiaryBankName().isEmpty()) {
				throw new CommonException(FundtransferConstants.BENIFICIARY_BANK_NAME);
			}
			benificiary.setBenificiaryBankName(benificiaryRequestDto.getBenificiaryBankName());

			if (benificiaryRequestDto.getBenificiaryName().isEmpty()) {
				throw new CommonException(FundtransferConstants.BENIFICIARY_NAME);
			}
			benificiary.setBenificiaryName(benificiaryRequestDto.getBenificiaryName());

			if (benificiaryRequestDto.getIfscCode().isEmpty()) {
				throw new CommonException(FundtransferConstants.IFSC_CODE);
			}
			benificiary.setIfscCode(benificiaryRequestDto.getIfscCode());
			benificiary.setBenificiaryAccountBalance(benificiaryRequestDto.getBenificiaryAccountBalance());
			benificiary.setUser(user);
			benificiaryRepository.save(benificiary);
			
			/*
			 * Account account=accountRepository.findAccountByUserId(userId);
			 * account.setBenificiaryId(benificiaryUp.getBenificiaryId());
			 * accountRepository.save(account);
			 */
		
		}

		for (Benificiary benificiaryy : benificiaries) {



			if (benificiaryy.getBenificiaryAccountNo() == benificiaryRequestDto.getBenificiaryAccountNo()) {

				throw new CommonException(FundtransferConstants.BENIFICIARY_EXITS);

			}

			Benificiary benificiary = new Benificiary();

			benificiary.setBenificiaryAccountNo(benificiaryRequestDto.getBenificiaryAccountNo());



			if (benificiaryRequestDto.getBenificiaryAccountType().isEmpty()) {

				throw new CommonException(FundtransferConstants.BENIFICIARY_ACCOUNT_TYPE);

			}

			benificiary.setBenificiaryAccountType(benificiaryRequestDto.getBenificiaryAccountType());



			if (benificiaryRequestDto.getBenificiaryBankName().isEmpty()) {

				throw new CommonException(FundtransferConstants.BENIFICIARY_BANK_NAME);

			}

			benificiary.setBenificiaryBankName(benificiaryRequestDto.getBenificiaryBankName());



			if (benificiaryRequestDto.getBenificiaryName().isEmpty()) {

				throw new CommonException(FundtransferConstants.BENIFICIARY_NAME);

			}

			benificiary.setBenificiaryName(benificiaryRequestDto.getBenificiaryName());



			if (benificiaryRequestDto.getIfscCode().isEmpty()) {

				throw new CommonException(FundtransferConstants.IFSC_CODE);

			}

			benificiary.setIfscCode(benificiaryRequestDto.getIfscCode());
			benificiary.setBenificiaryAccountBalance(benificiaryRequestDto.getBenificiaryAccountBalance());
			
			benificiary.setUser(user);

			benificiaryRepository.save(benificiary);

		}

		BenificiaryResponseDto benificiaryResponseDto = new BenificiaryResponseDto();
		benificiaryResponseDto.setMessage("added successfully");
		

		return benificiaryResponseDto;
	}

}
