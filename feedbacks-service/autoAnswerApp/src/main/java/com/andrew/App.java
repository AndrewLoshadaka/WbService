package com.andrew;


import com.andrew.api.FeedbackApi;
import com.andrew.db.ParamsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class App {
    public static void main( String[] args ) {
        ParamsService service = new ParamsService();
        if((Integer) service.getParamsMap().get("is_work") == 0) {
            System.out.println("Access unable!");
        }
        else {
            LocalDate date = (LocalDate) service.getParamsMap().get("date");
            long timestamp = 0;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formattedDate = date.format(dateFormatter);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date dateNew = sdf.parse(formattedDate);
                timestamp =  dateNew.getTime() / 1000; //current day
            } catch (ParseException e) {
                e.printStackTrace();
            }

            FeedbackApi api = new FeedbackApi();
            api.sendAnswer(timestamp, (int) service.getParamsMap().get("skip"));
        }
    }
}
