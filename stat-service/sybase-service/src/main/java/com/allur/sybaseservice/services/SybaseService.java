package com.allur.sybaseservice.services;

import com.allur.sybaseservice.connection.ConnectionDB;
import com.allur.sybaseservice.entity.StockEntity;
import com.allur.sybaseservice.entity.StockOTKEntity;
import com.allur.sybaseservice.entity.OTKEntity;
import com.allur.sybaseservice.entity.requestsdto.DateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SybaseService {
    private final Connection connection = ConnectionDB.getConnection();

    public Collection<StockOTKEntity> getResultStockAndOTK(@RequestParam Long dateFromL, @RequestParam Long dateToL){
        String dateFrom = convertDate(dateFromL);
        String dateTo = convertDate(dateToL);

        Map<CustomKey, StockOTKEntity> stockOTKEntityMap = new LinkedHashMap<>();

        Map<CustomKey, StockEntity> stockMap = getStock(dateFrom, dateTo);
        Map<CustomKey, OTKEntity> otkMap = getOtk(dateFrom, dateTo);

        /*otk > stock -> result = otk, for on stock
        * stock > otk -> result = stock, for on otk */
        if(otkMap.size() > stockMap.size()){
            for(Map.Entry<CustomKey, OTKEntity> elem : otkMap.entrySet()){
                stockOTKEntityMap.put(elem.getKey(),
                        new StockOTKEntity(
                            elem.getValue().getNmId(),
                            elem.getValue().getSize(),
                            elem.getValue().getWBDesign(),
                            elem.getValue().getCountProduct(),
                            0,
                            elem.getValue().getCountRoute()
                        ));
            }

            for(Map.Entry<CustomKey, StockEntity> elem : stockMap.entrySet()){
                CustomKey key = elem.getKey();
                if(stockOTKEntityMap.get(key) != null){
                    StockOTKEntity currentStockOtkEntity = stockOTKEntityMap.get(key);
                    StockOTKEntity newStockOTKEntity = new StockOTKEntity(
                            currentStockOtkEntity.getNmId(),
                            currentStockOtkEntity.getSize(),
                            currentStockOtkEntity.getWbDesign(),
                            currentStockOtkEntity.getCountOTK(),
                            elem.getValue().getCount(),
                            currentStockOtkEntity.getCountRoute()
                    );
                    stockOTKEntityMap.replace(key, currentStockOtkEntity, newStockOTKEntity);
                } else {
                    StockOTKEntity newStockOTKEntity = new StockOTKEntity(
                            elem.getValue().getNmId(),
                            elem.getValue().getSize(),
                            elem.getValue().getWbDesign(),
                            0,
                            elem.getValue().getCount(),
                            0
                    );
                    CustomKey customKey = new CustomKey(elem.getValue().getNmId(), elem.getValue().getSize());
                    stockOTKEntityMap.put(customKey,
                            newStockOTKEntity);
                }
            }
        } else {
            for(Map.Entry<CustomKey, StockEntity> elem : stockMap.entrySet()){
                stockOTKEntityMap.put(elem.getKey(),
                        new StockOTKEntity(
                                elem.getValue().getNmId(),
                                elem.getValue().getSize(),
                                elem.getValue().getWbDesign(),
                                0,
                                elem.getValue().getCount(),
                                0
                        ));
            }

            for (Map.Entry<CustomKey, OTKEntity> elem : otkMap.entrySet()){
                CustomKey key = elem.getKey();
                if (stockOTKEntityMap.get(key) != null) {
                    StockOTKEntity currentStockOtkEntity = stockOTKEntityMap.get(key);
                    StockOTKEntity newStockOTKEntity = new StockOTKEntity(
                            currentStockOtkEntity.getNmId(),
                            currentStockOtkEntity.getSize(),
                            currentStockOtkEntity.getWbDesign(),
                            elem.getValue().getCountProduct(),
                            currentStockOtkEntity.getCountStock(),
                            elem.getValue().getCountRoute()
                    );
                    stockOTKEntityMap.replace(key, currentStockOtkEntity, newStockOTKEntity);
                } else {
                    StockOTKEntity newStockOTKEntity = new StockOTKEntity(
                            elem.getValue().getNmId(),
                            elem.getValue().getSize(),
                            elem.getValue().getWBDesign(),
                            elem.getValue().getCountProduct(),
                            0,
                            elem.getValue().getCountRoute()
                    );
                    CustomKey customKey = new CustomKey(elem.getValue().getNmId(), elem.getValue().getSize());
                    stockOTKEntityMap.put(customKey,
                            newStockOTKEntity);
                }
            }
        }
        System.out.println(stockOTKEntityMap.size());
        return stockOTKEntityMap.values();
    }


    public Map<CustomKey, OTKEntity> getOtk (String dateFrom, String dateTo){
        Map<CustomKey, OTKEntity> otkMap = new LinkedHashMap<>();
        List<StockOTKEntity> entityList = new ArrayList<>();
        try {
            String query = "select ДизайнWB, АртикулWB, sum(кол_во_изделий) кол_во_изделий_, count(*) маршруток, размер from (\n" +
                    "select внкода, размер, ДизайнWB, АртикулWB, max(кол_во_изделий) кол_во_изделий  from\n" +
                    "(\n" +
                    "select\n" +
                    "Маршрутки.внкода,\n" +
                    "of_Razm_S_Rost(Модели_в_маршрутке.размер) размер,\n" +
                    "Перемещение_изделий.Кол_во кол_во_изделий,\n" +
                    "of_common_design_of_mod_in_ML_new(Модели_в_маршрутке.внкода) ДизайнWB,\n" +
                    "of_ArtWB_of_mod_in_ML_new(Модели_в_маршрутке.внкода) АртикулWB\n" +
                    "from Накладные_перемещения_изделий\n" +
                    "join Перемещение_изделий\n" +
                    "join Маршрутки\n" +
                    "on Маршрутки.Внкода=Перемещение_изделий.Маршрутка\n" +
                    "left outer join Модели_в_маршрутке\n" +
                    "on Модели_в_маршрутке.Маршрутка=Маршрутки.Внкода\n" +
                    "where (Накладные_перемещения_изделий.оформлен = 1) and (Накладные_перемещения_изделий.Дата between ? and ?)\n" +
                    "and (Перемещение_изделий.Маршрутка is not null)\n" +
                    "and (Накладные_перемещения_изделий.куда = 8)\n" +
                    "\n" +
                    "union all\n" +
                    "\n" +
                    "select\n" +
                    "Маршрутки.внкода,\n" +
                    "of_Razm_S_Rost(Модели_в_маршрутке.размер) размер,\n" +
                    "Модели_в_маршрутке.Кол_во кол_во_изделий,\n" +
                    "of_common_design_of_mod_in_ML_new(Модели_в_маршрутке.внкода) ДизайнWB,\n" +
                    "of_ArtWB_of_mod_in_ML_new(Модели_в_маршрутке.внкода) АртикулWB\n" +
                    "from ПИ_Перемещение_изделий\n" +
                    "join Маршрутки\n" +
                    "on Маршрутки.Внкода = ПИ_Перемещение_изделий.Маршрутка\n" +
                    "left outer join Модели_в_маршрутке\n" +
                    "on Модели_в_маршрутке.Маршрутка=Маршрутки.Внкода\n" +
                    "where (ПИ_Перемещение_изделий.Дата between ? and ?)\n" +
                    "and (ПИ_Перемещение_изделий.Маршрутка is not null)\n" +
                    "and (ПИ_Перемещение_изделий.куда = 8)\n" +
                    "\n" +
                    ") as rrr\n" +
                    "group by внкода, размер, ДизайнWB, АртикулWB) as ddd\n" +
                    "group by размер, ДизайнWB, АртикулWB";

            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dateFrom);
            preparedStatement.setString(2, dateTo);
            preparedStatement.setString(3, dateFrom);
            preparedStatement.setString(4, dateTo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getString(2) != null){
                    CustomKey customKey = new CustomKey(resultSet.getString(2), resultSet.getString(5));
                    otkMap.put(customKey, parseToEntity(resultSet));
                }
                    //entityList.add(parseToEntity(resultSet));
                else
                    System.out.println("null!");
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        System.out.println("otk map size - " + otkMap.size());
        return otkMap;
    }

    private Map<CustomKey, StockEntity> getStock(String dateFrom, String dateTo){
        Map<CustomKey, StockEntity> stockMap = new LinkedHashMap<>();
        List<StockOTKEntity> stockEntityList = new ArrayList<>();
        try {
            String query = "select Размер,ДизайнWB, АртикулWB, sum(кол_во) кол_во_изделий_ from (\n" +
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
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dateFrom);
            preparedStatement.setString(2, dateTo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getString(3) != null){
                    CustomKey customKey = new CustomKey(resultSet.getString(3), resultSet.getString(1));
                    stockMap.put(customKey, parseToStockEntity(resultSet));
                }
                else
                    System.out.println("null!");
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }

        System.out.println("stock map size - " + stockMap.size());
        return stockMap;
    }
    private StockEntity parseToStockEntity(ResultSet resultSet) throws SQLException{
        return new StockEntity(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getInt(4)
        );
    }
    private OTKEntity parseToEntity(ResultSet resultSet) throws SQLException {
        return new OTKEntity(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getInt(4),
                resultSet.getString(5));
    }

    private String convertDate(long ms){
        Date date = new Date(ms);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomKey{
        private String nmId;
        private String size;
    }
}
