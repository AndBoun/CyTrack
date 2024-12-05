package com.mockito.account;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

	Account save(Account account);

	void delete(Account account);

	Account getAccountByID(int id);

	List<Account> getAccountList();

}
