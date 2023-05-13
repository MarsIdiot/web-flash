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
        //识别一：7+8+9+10 各10块
        String data = buyData.getBuyData();
        String[] splitBlack = data.split(" ");
        if(splitBlack != null && splitBlack.length >= 2){
            if(StringUtils.isNoneBlank(splitBlack[0])) {
                String[] splitSum = splitBlack[0].split("\\+");
                int buyCount = 0;
                if (StringUtils.isNoneBlank(splitBlack[1]) && splitBlack[1].length() >= 3) {
                    buyCount = Integer.valueOf(splitBlack[1].substring(1,2));
                }
                for(int i=0;i<splitSum.length;i++){
                    Buy buy = new Buy();
                    buy.setBuyId(buyId);
                    buy.setBuyName(buyName);
                    buy.setBuyCode(Integer.valueOf(splitSum[i]));
                    buy.setBuyCount(buyCount);
                    buyList.add(buy);
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
