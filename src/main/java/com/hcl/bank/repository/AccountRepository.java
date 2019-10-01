package com.hcl.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.bank.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	public Account findByAccountNumber(long fromAccountNo);
	
	@Query("select a from Account a where a.user.userId = :userId")
	
	public Account findAccountByUserId(int userId);

}
