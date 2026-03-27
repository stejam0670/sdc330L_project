package com.alexjames.bankaccountmanagement.controllers;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountForm;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.CheckingAccount;
import com.alexjames.bankaccountmanagement.models.IRAAccount;
import com.alexjames.bankaccountmanagement.models.SavingsAccount;
import com.alexjames.bankaccountmanagement.services.AccountService;
import java.util.List;
import org.springframework.stereotype.Controller;
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

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "form";
    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute("accountForm") AccountForm accountForm,
                              RedirectAttributes redirectAttributes) {
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
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

    private Account convertFormToAccount(AccountForm accountForm) {
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
