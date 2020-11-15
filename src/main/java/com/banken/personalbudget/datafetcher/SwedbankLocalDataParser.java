package com.banken.personalbudget.datafetcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SwedbankLocalDataParser {

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
                .skip(2) // headers
                .map(this::parseLine)
                .collect(Collectors.toList());
    }

    public Transaction parseLine(String line) {
        String[] split = line.split(",");
        Transaction transaction = new Transaction();
        transaction.setBank("Swedbank");
        transaction.setTransactionDate(split[6]);
        transaction.setOtherParty(split[9].replace("\"", ""));
        transaction.setAmount(split[10]);
        transaction.setAccountName(split[3].replace("\"", ""));
        return transaction;
    }
}
