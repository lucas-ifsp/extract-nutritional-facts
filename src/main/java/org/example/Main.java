package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        var path = Paths.get("src/main/resources/nutrition.csv");
        try (var reader = Files.newBufferedReader(path)) {
            reader.lines()
                    .skip(1) //cabeçalho
                    .map(Main::removeQuotesBetweenQuotes)
                    .map(Main::replaceCommaBySemicolon)
                    .map(Main::replaceSemicolonByCommaBetweenQuotes)
                    .map(Main::removeQuotes)
                    .map(Main::toAlimento)
                    .forEach(System.out::println);
        }
    }

    private static String removeQuotesBetweenQuotes(String line) {
        return line.replace("\"\"", "");
    }

    private static String replaceCommaBySemicolon(String line) {
        return line.replace(",", ";");
    }

    private static String replaceSemicolonByCommaBetweenQuotes(String line){
        final Pattern textBetweenQuotesPattern = Pattern.compile("\"(.*)\"");
        final Matcher matcher = textBetweenQuotesPattern.matcher(line);
        if(!matcher.find()) return line;
        final String textBetweenQuotes = matcher.group();
        return line.replace(textBetweenQuotes, textBetweenQuotes.replace(";", ","));
    }

    private static String removeQuotes(String line) {
        return line.replace("\"", "");
    }

    private static Alimento toAlimento(String line){
        final String[] data = line.split(";");
        Integer id = Integer.parseInt(data[0]);
        String nome = data[1];
        Integer porcao = Integer.parseInt(extractNumberPart(data[2]));
        Integer calorias = Integer.valueOf(data[3]);
        Double gordurasSaturadas = Double.valueOf(extractNumberPart(data[5]));
        Integer colesterol = Integer.valueOf(extractNumberPart(data[6]));
        Double proteina = Double.valueOf(extractNumberPart(data[38]));
        boolean hasGluten = hasGluten(data[1]);
        Double sodio = Double.valueOf(extractNumberPart(data[7]));
        Double acucar = Double.valueOf(extractNumberPart(data[60]));
        Double lactose = Double.valueOf(extractNumberPart(data[64]));
        return new Alimento(id,nome,porcao,calorias, colesterol,
                proteina, hasGluten, gordurasSaturadas, sodio, acucar, lactose);
    }

    private static boolean hasGluten(String food) {
        final List<String> foodsWithGluten = List.of("bread", "pasta", "pie", "cookie");
        return foodsWithGluten.stream().anyMatch(food::contains);
    }

    private static String extractNumberPart(String text){
        if(text == null || text.isEmpty()) return "0";
        Pattern numberPattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = numberPattern.matcher(text);
        if (matcher.find()) return matcher.group();
        return text;
    }
}