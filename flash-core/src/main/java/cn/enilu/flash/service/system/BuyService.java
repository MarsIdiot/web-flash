package cn.enilu.flash.service.system;

import cn.enilu.flash.bean.entity.system.Buy;
import cn.enilu.flash.bean.entity.system.BuyData;
import cn.enilu.flash.cache.ConfigCache;
import cn.enilu.flash.dao.system.BuyRepository;
import cn.enilu.flash.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * CfgService
 *
 * @author enilu
 * @version 2018/11/17 0017
 */

@Service
@Transactional
public class BuyService extends BaseService<Buy, Long, BuyRepository> {
    @Autowired
    private ConfigCache configCache;

    public List<Buy> transfer(BuyData buyData) {
        List<Buy> buyList = new ArrayList<>();

        String buyId = buyData.getBuyId();
        String buyName = buyData.getBuyName();
        //识别
        String data = buyData.getBuyData();
        String[] splitAll = data.split("\n");
        if(splitAll != null && splitAll.length >= 1){
            for(int i=0;i<splitAll.length;i++){
                String splitLine= splitAll[i];
                if(StringUtils.isNoneBlank(splitLine) && splitLine.contains("各")) {
                    //识别一：7+8+9+10 各10块
                    String[] splitBlack = splitLine.split(" ");
                    if(splitBlack != null && splitBlack.length >= 2){
                        if(StringUtils.isNoneBlank(splitBlack[0])) {
                            String[] splitSum = splitBlack[0].split("\\+");
                            int buyCount = 0;
                            if (StringUtils.isNoneBlank(splitBlack[1]) && splitBlack[1].length() >= 2) {
                                buyCount = Integer.valueOf(splitBlack[1].substring(1,splitBlack[1].length()-1));
                            }
                            for(int j=0;j<splitSum.length;j++){
                                Buy buy = new Buy();
                                buy.setBuyId(buyId);
                                buy.setBuyName(buyName);
                                buy.setBuyCode(Integer.valueOf(splitSum[j]));
                                buy.setBuyCount(buyCount);
                                buyList.add(buy);
                            }
                        }
                    }
                }else if(StringUtils.isNoneBlank(splitLine) && splitLine.contains("/")) {
                    //识别二：7/10 8/20 9/30
                    String[] splitBlack = splitLine.split(" ");
                    if(splitBlack != null && splitBlack.length >= 1){
                        for(int j=0;j<splitBlack.length;j++){
                            if(StringUtils.isNoneBlank(splitBlack[j]) && splitBlack[j].length() >= 3) {
                                String[] splitOne = splitBlack[j].split("/");
                                if(splitOne != null && splitOne.length == 2){
                                    Buy buy = new Buy();
                                    buy.setBuyId(buyId);
                                    buy.setBuyName(buyName);
                                    buy.setBuyCode(Integer.valueOf(splitOne[0]));
                                    buy.setBuyCount(Integer.valueOf(splitOne[1]));
                                    buyList.add(buy);
                                }
                            }
                        }
                    }
                }
            }
        }
        return buyList;
    }

    public void add(List<Buy> buyList) {
         batchInsert(buyList);
    }

    public Buy saveOrUpdate(Buy buy) {
        if (buy.getId() == null) {
            insert(buy);
        } else {
            update(buy);
        }
        return buy;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        configCache.cache();
    }

}
