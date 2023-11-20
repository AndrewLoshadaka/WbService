package com.allur.wbstatserver.service;

import com.allur.wbstatserver.model.dto.SybaseDTO;
import com.allur.wbstatserver.model.dto.requestsDTO.SybaseRequestBody;
import com.allur.wbstatserver.model.entities.StatEntity;
import com.allur.wbstatserver.repositories.StatRepository;
import com.allur.wbstatserver.sybaseapi.SybaseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;
    private final SybaseApi sybaseApi;

    public List<SybaseDTO> getEntities(@RequestBody SybaseRequestBody dto){
        List answerList =  sybaseApi.getSybaseInform(dto.getDateFrom(), dto.getDateTo());
        List<Integer> temp = getListNmIds(answerList);

        List<StatEntity> statEntities = statRepository.findByNmIdIn(temp);
        Map<Integer, StatEntity> entityMap = new HashMap<>();
        for(StatEntity s : statEntities){
            entityMap.put(s.getNmId(), s);
        }

        List<SybaseDTO> resultSybase = new ArrayList<>();

        for(Object o : answerList){
            LinkedHashMap linkedHashMap = (LinkedHashMap) o;
            String key = linkedHashMap.get("nmId").toString();
            SybaseDTO sybaseDTO = new SybaseDTO(
                    Integer.parseInt(linkedHashMap.get("countOTK").toString()),
                    Integer.parseInt(linkedHashMap.get("countStock").toString()),
                    Integer.parseInt(linkedHashMap.get("countRoute").toString()),
                    linkedHashMap.get("size").toString(),
                    linkedHashMap.get("wbDesign").toString(),
                    entityMap.get(Integer.parseInt(key)).getPrGroup(),
                    entityMap.get(Integer.parseInt(key)).getTpStream()
                    );
            resultSybase.add(sybaseDTO);
        }
        return resultSybase;
    }

    private List<Integer> getListNmIds(List list){
        List<Integer> temp = new ArrayList<>();
        for(Object o : list){
            temp.add(Integer.valueOf(((LinkedHashMap<String, String>) o).get("nmId")));
        }
        return temp;
    }
}
