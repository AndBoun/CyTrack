package com.mockito.account;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id = 0;

	@NotNull
	// @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "UserID")
	private String userID;

	@NotNull
	@Column(name = "Password")
	private String password;

	@NotNull
	@Column(name = "Email")
	private String email;

	@NotNull
	@Column(name = "Creation_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfCreation;

	public Account() {
		this.setDateOfCreation();
	}

	public Account(int id, String user, String password, String email) {
		this.id = id;
		this.userID = user;
		this.password = password;
		this.email = email;
		this.setDateOfCreation();
	}

	public String getUserID() {

		return this.userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateOfCreation() {

		return dateOfCreation;
	}

	public void setDateOfCreation() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		dtf.format(localDate);
		java.sql.Date dat = java.sql.Date.valueOf(localDate);
		this.dateOfCreation = dat;
	}

}
