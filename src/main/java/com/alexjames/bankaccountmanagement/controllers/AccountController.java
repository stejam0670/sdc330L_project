package com.alexjames.bankaccountmanagement.controllers;

import com.alexjames.bankaccountmanagement.models.Account;
import com.alexjames.bankaccountmanagement.models.AccountForm;
import com.alexjames.bankaccountmanagement.models.AccountHolder;
import com.alexjames.bankaccountmanagement.models.CheckingAccount;
import com.alexjames.bankaccountmanagement.models.IRAAccount;
import com.alexjames.bankaccountmanagement.models.SavingsAccount;
import com.alexjames.bankaccountmanagement.models.Transaction;
import com.alexjames.bankaccountmanagement.models.TransactionForm;
import com.alexjames.bankaccountmanagement.services.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/accounts/{id}/transactions")
    public String showTransactions(@PathVariable long id, Model model) {
        populateTransactionPage(model, accountService.getAccountById(id), new TransactionForm(), new TransactionForm());
        return "transactions";
    }

    @PostMapping("/accounts/{id}/transactions/deposit")
    public String deposit(@PathVariable long id,
                          @Valid @ModelAttribute("depositForm") TransactionForm depositForm,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        Account account = accountService.getAccountById(id);

        if (bindingResult.hasErrors()) {
            populateTransactionPage(model, account, depositForm, new TransactionForm());
            model.addAttribute("transactionErrorMessage", "Please correct the deposit fields and try again.");
            return "transactions";
        }

        accountService.deposit(id, depositForm.getTransactionName(), depositForm.getAmount());
        redirectAttributes.addFlashAttribute("transactionSuccessMessage", "Deposit saved successfully.");
        return "redirect:/accounts/" + id + "/transactions";
    }

    @PostMapping("/accounts/{id}/transactions/withdraw")
    public String withdraw(@PathVariable long id,
                           @Valid @ModelAttribute("withdrawForm") TransactionForm withdrawForm,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        Account account = accountService.getAccountById(id);

        if (bindingResult.hasErrors()) {
            populateTransactionPage(model, account, new TransactionForm(), withdrawForm);
            model.addAttribute("transactionErrorMessage", "Please correct the withdrawal fields and try again.");
            return "transactions";
        }

        accountService.withdraw(id, withdrawForm.getTransactionName(), withdrawForm.getAmount());
        redirectAttributes.addFlashAttribute("transactionSuccessMessage", "Withdrawal saved successfully.");
        return "redirect:/accounts/" + id + "/transactions";
    }

    @PostMapping("/accounts/{accountId}/transactions/{transactionId}/rollback")
    public String rollbackTransaction(@PathVariable long accountId,
                                      @PathVariable long transactionId,
                                      RedirectAttributes redirectAttributes) {
        Transaction transaction = accountService.rollbackTransaction(accountId, transactionId);
        redirectAttributes.addFlashAttribute(
                "transactionSuccessMessage",
                transaction.getTransactionType().getLabel() + " rolled back and removed from history.");
        return "redirect:/accounts/" + accountId + "/transactions";
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

    private void populateTransactionPage(Model model, Account account, TransactionForm depositForm, TransactionForm withdrawForm) {
        List<Transaction> transactions = accountService.getTransactionsForAccount(account.getId());
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("depositForm", depositForm);
        model.addAttribute("withdrawForm", withdrawForm);
    }
}
