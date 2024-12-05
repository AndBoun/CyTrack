package com.mockito.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

	@Autowired
	private AccountRepository repo;

	public Account getAccountByID(int id) {
		return repo.getAccountByID(id);
	}

	public List<Account> getAccountList() {
		return repo.getAccountList();
	}

}
