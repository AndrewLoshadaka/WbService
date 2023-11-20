package com.andrew.api;

import com.andrew.model.Answer;
import com.andrew.model.FeedbackDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.*;

// TODO: 15.09.2023 подключение к API и получение всех отзывов

public class FeedbackApi {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, List<AnswerDTO>> map = new HashMap<>();
    public List<FeedbackDetails> getFeedbackDetails (long date) {
        List<FeedbackDetails> result = new ArrayList<>();
        try {
            URL url = new URL("http://192.168.208.235:9091/api/feedbacks/empty?date=" + date);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String jsonResponse = readResponse(inputStream);

                FeedbackDetails[] feedbackArray = objectMapper.readValue(jsonResponse, FeedbackDetails[].class);
                Collections.addAll(result, feedbackArray);
            } else {
                System.out.println("Ошибка HTTP: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void sendAnswer(long date, int skip) {
        List<FeedbackDetails> feedbackDetails = getFeedbackDetails(date);
        for(int i = 0; i < feedbackDetails.size(); i++){
            if((i + 1) % skip == 0 && skip != 1)
                i++;

            FeedbackDetails x = feedbackDetails.get(i);

            if (createAnswer(x.getProductName(), x.getBrandName(), x.getSupplier()) != null) {
                Answer answer = new Answer();
                answer.setId(x.getId());
                answer.setText(createAnswer(x.getProductName(), x.getBrandName(), x.getSupplier()));
                answer.setBrandName(x.getBrandName());
                answer.setSupplier(x.getSupplier());


                addTestInform(answer.getSupplier(), answer.getId(), answer.getText());

                try {
                    URL url = new URL("http://192.168.208.235:9091/api/feedbacks/send-answer");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    String jsonInputString = objectMapper.writeValueAsString(answer);
                    System.out.println(jsonInputString);
                    OutputStream outputStream = connection.getOutputStream();
                    byte[] input = jsonInputString.getBytes("utf-8");
                    outputStream.write(input, 0, input.length);

                    int responseCode = connection.getResponseCode();
                    //System.out.println("Код ответа: " + responseCode);

                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        show();
    }

    private String readResponse(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private String createAnswer(String productName, String brand, String supplier){
        String answer = null;
        try {
            URL url = new URL("http://192.168.208.235:9091/api/feedbacks/answer");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            Map<String, String> answerMap = new HashMap<>();
            answerMap.put("product", productName);
            answerMap.put("brand", brand);
            answerMap.put("supplier", supplier);

            String jsonInputString = objectMapper.writeValueAsString(answerMap);
            OutputStream outputStream = connection.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            outputStream.write(input, 0, input.length);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Map result = objectMapper.readValue(inputStream, Map.class);
                answer = (String) result.get("answer");
            } else {
                System.out.println("Ошибка HTTP1: " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(answer == null)
            return null;

        answer += " С уважением, " + brand;
        return answer;
    }

    private void addTestInform(String supplier, String id, String text){
        AnswerDTO answerDTO = new AnswerDTO(id, text);
        map.computeIfAbsent(supplier, k -> new ArrayList<>());
        map.get(supplier).add(answerDTO);
    }

    private void show(){
        for(Map.Entry<String, List<AnswerDTO>> x : map.entrySet()){
            System.out.println(x.getKey());
            for(AnswerDTO a : x.getValue()){
                System.out.println(a.getId() + "; " + a.getText());
            }
        }
    }
}

@Getter
@Setter
@AllArgsConstructor
class AnswerDTO{
    private String id;
    private String text;
}
