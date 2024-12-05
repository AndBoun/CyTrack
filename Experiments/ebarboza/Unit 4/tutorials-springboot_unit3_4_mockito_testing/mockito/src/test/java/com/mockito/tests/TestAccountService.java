package com.mockito.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mockito.account.Account;
import com.mockito.account.AccountRepository;
import com.mockito.account.AccountService;

public class TestAccountService {

	@InjectMocks
	AccountService acctService;

	@Mock
	AccountRepository repo;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAccountByIdTest() {
		when(repo.getAccountByID(1)).thenReturn(new Account(1, "jDoe", "123456", "jDoe@gmail.com"));

		Account acct = acctService.getAccountByID(1);

		assertEquals("jDoe", acct.getUserID());
		assertEquals("123456", acct.getPassword());
		assertEquals("jDoe@gmail.com", acct.getEmail());
	}

	@Test
	public void getAllAccountTest() {
		List<Account> list = new ArrayList<Account>();
		Account acctOne = new Account(1, "John", "1234", "john@gmail.com");
		Account acctTwo = new Account(2, "Alex", "abcd", "alex@yahoo.com");
		Account acctThree = new Account(3, "Steve", "efgh", "steve@gmail.com");

		list.add(acctOne);
		list.add(acctTwo);
		list.add(acctThree);

		when(repo.getAccountList()).thenReturn(list);

		List<Account> acctList = acctService.getAccountList();

		assertEquals(3, acctList.size());
		verify(repo, times(1)).getAccountList();
	}

}
