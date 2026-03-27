package com.alexjames.bankaccountmanagement.models;

/**
 * Name: Alex James
 * Date: 2026-03-26
 * Purpose: Defines the contract for account types that can apply interest.
 */
// Interface demonstration: classes that earn interest implement this shared behavior.
public interface InterestEligible {
    void applyInterest();
}
