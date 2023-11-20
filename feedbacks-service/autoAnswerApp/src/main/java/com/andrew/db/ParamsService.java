package com.andrew.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class ParamsService {
    private final Connection connection = ConnectionDB.getConnection();
    private final Map<String, Object> paramsMap = new HashMap<>();

    public Map<String, Object> getParamsMap(){
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from answer_params_auto");
            resultSet.next();

            paramsMap.put("is_work", resultSet.getInt("is_work"));
            paramsMap.put("date", resultSet.getDate("date").toLocalDate());
            paramsMap.put("skip", resultSet.getInt("skip"));

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        return paramsMap;
    }

}
