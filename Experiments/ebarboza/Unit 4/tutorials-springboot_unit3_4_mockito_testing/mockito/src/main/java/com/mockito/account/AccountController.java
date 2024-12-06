package com.mockito.account;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	private final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@ModelAttribute("account")
	public Account newAccount() {
		return new Account();
	}

	@RequestMapping(value = "/accounts/register", method = RequestMethod.GET)
	public ModelAndView register(Model model, HttpServletRequest request) {
		logger.info("Entered into get accounts register controller Layer");
		String view = "accounts/registration";
		return new ModelAndView(view, "command", model);
	}

	@RequestMapping(value = "/accounts/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("account") Account account) {
		logger.info("Entered into post account registration controller Layer");
		return "accounts/login";
	}

	@RequestMapping(value = "/accounts/login", method = RequestMethod.GET)
	public ModelAndView log(Model model, HttpServletRequest request) {
		logger.info("Entered into get accounts login controller Layer");
		String view = "accounts/login";
		return new ModelAndView(view, "command", model);
	}
}
