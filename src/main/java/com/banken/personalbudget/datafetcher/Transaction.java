package com.banken.personalbudget.datafetcher;

import com.banken.personalbudget.Common;

import java.util.Arrays;
import java.util.List;

public class Transaction {
    public static final Transaction EMPTY = new Transaction();
    private static final String TAG_DIVIDER = "->";

    private static final List<String> SAME_ACCOUNT = Arrays.asList("541256******6401", "541256******7905", "541256******9618");


    static {
        EMPTY.setOtherParty("");
        EMPTY.setTransactionDate("");
        EMPTY.setAmount("");

        EMPTY.setBank("");
        EMPTY.setAccountName("");
        EMPTY.setOwner("");

        EMPTY.setDescription("");
        EMPTY.setTag("");
    }

    private String otherParty;
    private String transactionDate;
    private String amount;

    private String bank;
    private String accountName;
    private String owner;

    private String description;
    private String tag;

    private boolean ignore;
    private boolean treatAsIncome;
    private boolean newTransaction;

    /**
     * Does this instance refer to the same transaction as other?
     *
     * @return
     */
    public boolean sameTransactionAs(Transaction other) {
        // If any one of these parameters is empty, we can't really tell if it refers to the same
        // transaction or not. Return false in order to avoid false positives.
        if (isNullOrEmpty(otherParty) || isNullOrEmpty(transactionDate) ||
                isNullOrEmpty(amount) || isNullOrEmpty(owner) ||
                isNullOrEmpty(accountName)) {
            return false;
        }

        return otherParty.equals(other.otherParty) &&
                transactionDate.equals(other.transactionDate) &&
                amount.equals(other.amount) &&
                owner.equals(other.owner) &&
                accountNameEquals(accountName, other.accountName);
    }

    private static boolean accountNameEquals(String thisName, String thatName) {
        return thisName.equals(thatName) || SAME_ACCOUNT.contains(thisName) && SAME_ACCOUNT.contains(thatName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (ignore != that.ignore) return false;
        if (otherParty != null ? !otherParty.equals(that.otherParty) : that.otherParty != null) return false;
        if (transactionDate != null ? !transactionDate.equals(that.transactionDate) : that.transactionDate != null)
            return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (bank != null ? !bank.equals(that.bank) : that.bank != null) return false;
        if (accountName != null ? !accountName.equals(that.accountName) : that.accountName != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return tag != null ? tag.equals(that.tag) : that.tag == null;
    }

    @Override
    public int hashCode() {
        int result = otherParty != null ? otherParty.hashCode() : 0;
        result = 31 * result + (transactionDate != null ? transactionDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (bank != null ? bank.hashCode() : 0);
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (ignore ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "otherParty='" + otherParty + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", amount='" + amount + '\'' +
                ", bank='" + bank + '\'' +
                ", accountName='" + accountName + '\'' +
                ", owner='" + owner + '\'' +
                ", description='" + description + '\'' +
                ", tag='" + tag + '\'' +
                ", ignore=" + ignore +
                '}';
    }

    public static List<String> getNames() {
        return Arrays.asList("Other party", "Date", "Amount", "Bank", "Account name", "Account owner", "Description",
                "Tag", "Treat as income");
    }

    public List<String> getValues() {
        return Arrays.asList(otherParty, transactionDate, amount, bank, accountName, owner, description, tag, "" + treatAsIncome);
    }

    public List<String> getStaticValues() {
        return Arrays.asList(otherParty, transactionDate, amount, bank, accountName, owner);
    }

    public String getOtherParty() {
        return otherParty;
    }

    public void setOtherParty(String otherParty) {
        this.otherParty = otherParty;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public String getTopLevelTag() {
        if (Common.isEmpty(tag)) {
            return null;
        }
        return tag.split(TAG_DIVIDER)[0];
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public boolean isTreatAsIncome() {
        return treatAsIncome;
    }

    public void setTreatAsIncome(boolean treatAsIncome) {
        this.treatAsIncome = treatAsIncome;
    }

    public boolean isNewTransaction() {
        return newTransaction;
    }

    public void setNewTransaction(boolean newTransaction) {
        this.newTransaction = newTransaction;
    }
}
