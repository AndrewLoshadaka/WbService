package com.allur.sybaseservice;

import com.allur.sybaseservice.connection.ConnectionDB;
import com.allur.sybaseservice.entity.StockEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class SybaseServiceApplication {

    public static void main(String[] args) {
        /*String q = "select Размер,ДизайнWB, АртикулWB, sum(кол_во) кол_во_изделий_ from (\n" +
                "SELECT\n" +
                "of_ArtWB_of_mod_in_nakl_new(Модели_в_накладной.внкода) АртикулWB,\n" +
                "of_Razm_S_Rost(Модели_в_накладной.Размер) Размер,\n" +
                "Модели_в_накладной.Кол_во,\n" +
                "of_common_design_of_mod_in_NAKL_new(Модели_в_накладной.внкода) ДизайнWB\n" +
                "FROM Накладные Накладные\n" +
                "left outer join Модели_в_накладной\n" +
                "on (Модели_в_накладной.Накладная = Накладные.ВнКодА)\n" +
                "WHERE  (Накладные.Тип_накладной in (8))\n" +
                "and (Накладные.Дата_накладной between ? and ?)\n" +
                "and (Накладные.Оформлен=1)\n" +
                ")\n" +
                "as ddd\n" +
                "group by размер, ДизайнWB, АртикулWB";
        try{
            Connection connection = ConnectionDB.getConnection();
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(q);
            statement.setString(1, "01.11.2023");
            statement.setString(2, "02.11.2023");
            ResultSet resultSet = statement.executeQuery();
            List<StockEntity> list = new ArrayList<>();
            while (resultSet.next()){
                StockEntity stockEntity = new StockEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4)
                );
                list.add(stockEntity);
            }
            int i = 0;
            for(StockEntity s : list){
                System.out.println(s.getNmId() + " " + s.getSize() + s.getWbDesign() + s.getCount());
            }
        } catch (SQLException sqlException){

        }*/
        SpringApplication.run(SybaseServiceApplication.class, args);
    }

}
