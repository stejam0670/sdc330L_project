package com.alexjames.bankaccountmanagement.controllers;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountForm;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.CheckingAccount;
import com.alexjames.bankaccountmanagement.models.IRAAccount;
import com.alexjames.bankaccountmanagement.models.SavingsAccount;
import com.alexjames.bankaccountmanagement.services.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Handles account form submission and account display requests.
 */
@Controller
public class AccountController {
    private final AccountService accountService;

    // Constructor demonstration: Spring injects the service needed by this controller.
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        // MVC flow: the controller prepares an AccountForm object for Thymeleaf form binding.
        model.addAttribute("accountForm", new AccountForm());
        return "form";
    }

    @PostMapping("/form")
    public String processForm(@Valid @ModelAttribute("accountForm") AccountForm accountForm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formErrorMessage", "Please correct the highlighted fields and submit the form again.");
            return "form";
        }

        // MVC flow: submitted form data is converted into a domain object and passed to the service layer.
        Account account = convertFormToAccount(accountForm);
        accountService.addAccount(account);

        redirectAttributes.addFlashAttribute("savedAccountInfo", account.getAccountInfo());
        return "redirect:/results";
    }

    @GetMapping("/results")
    public String showResults() {
        return "results";
    }

    @GetMapping("/accounts")
    public String showAccounts(Model model) {
        // Polymorphism demonstration: multiple account subclasses are retrieved as a List<Account>.
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    private Account convertFormToAccount(AccountForm accountForm) {
        // Composition demonstration: each account is created with an AccountHolder object.
        AccountHolder holder = new AccountHolder(accountForm.getHolderName(), accountForm.getEmail());
        String accountType = accountForm.getAccountType();
        Double interestRate = accountForm.getInterestRate() == null ? 0.0 : accountForm.getInterestRate();

        if ("SAVINGS".equalsIgnoreCase(accountType)) {
            return new SavingsAccount(
                    accountForm.getAccountNumber(),
                    accountForm.getBalance(),
                    holder,
                    interestRate);
        }

        if ("IRA".equalsIgnoreCase(accountType)) {
            return new IRAAccount(
                    accountForm.getAccountNumber(),
                    accountForm.getBalance(),
                    holder,
                    interestRate);
        }

        return new CheckingAccount(
                accountForm.getAccountNumber(),
                accountForm.getBalance(),
                holder,
                0.0);
    }
}
