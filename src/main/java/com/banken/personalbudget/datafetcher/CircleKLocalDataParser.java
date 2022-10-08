package com.banken.personalbudget.datafetcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CircleKLocalDataParser {
    private final String year;

    public CircleKLocalDataParser(String year) {
        this.year = year;
    }

    public List<Transaction> parse(List<String> filenames) {
        return filenames.stream()
                .map(filename -> Paths.get(filename))
                .map(path -> {
                    try {
                        return Files.readAllLines(path, StandardCharsets.ISO_8859_1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(this::parseLinesInFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Transaction> parseLinesInFile(List<String> lines) {
        return lines.stream()
                .map(this::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Transaction parseLine(String line) {
        if (!line.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d,.*")) {
            return null;
        }
        String[] split = line.split(",");
        Transaction transaction = new Transaction();
        transaction.setBank("CircleK");
        transaction.setTransactionDate(split[0]);
        transaction.setOtherParty(split[2].replace("\"", ""));
        transaction.setAmount(swapSign(split[6]));
        transaction.setAccountName("circlek");
        return transaction;
    }

    private String swapSign(String amount) {
        return amount.startsWith("-") ? amount.substring(1) : "-" + amount;
    }

}
