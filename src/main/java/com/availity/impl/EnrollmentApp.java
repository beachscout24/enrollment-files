package com.availity.impl;

import com.availity.model.Enrollee;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnrollmentApp {

    public static void main(String[] args){
        List<Enrollee> enrollees = readFile();
        Map<String, List<Enrollee>> enrolleesListByInsuranceCompany = enrollees.stream()
                .collect(Collectors.groupingBy(Enrollee::getInsuranceCompany,
                        Collectors.toList()));
        enrolleesListByInsuranceCompany.forEach( (index,value) -> {
            List<Enrollee> finalEnrollees = value.stream().sorted(Comparator.comparingInt(Enrollee::getVersion)
                    .reversed()).filter(distinctByKey(Enrollee::getUserId)).collect(Collectors.toList());
            Collections.sort(value);
            writeToFiles(finalEnrollees);
        });

    }

    private static List<Enrollee> readFile(){
        String fileName = "./Enrollees.csv";
        List<Enrollee> enrollees = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            List<List<String>> values = lines.map(line -> Arrays.asList(line.split(","))).collect(Collectors.toList());
            values.forEach(
                    value -> {
                        Enrollee enrollee = new Enrollee();
                        enrollee.setUserId(value.get(0));
                        enrollee.setName(value.get(1));
                        enrollee.setVersion(Integer.valueOf(value.get(2)));
                        enrollee.setInsuranceCompany(value.get(3));
                        enrollees.add(enrollee);
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enrollees;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static void writeToFiles(List<Enrollee> enrollees){
        enrollees.forEach( enrollee -> {
            String insuranceCompany = enrollee.getInsuranceCompany();
            ObjectMapper mapper = new ObjectMapper();
            try {
                // Serialize Java object info JSON file.
                Files.write(new File(insuranceCompany + ".txt").toPath(), enrollee.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
