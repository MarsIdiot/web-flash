package cn.enilu.flash.api.controller.system;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.system.Buy;
import cn.enilu.flash.bean.entity.system.BuyData;
import cn.enilu.flash.bean.entity.system.Cfg;
import cn.enilu.flash.bean.entity.system.FileInfo;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.system.BuyService;
import cn.enilu.flash.service.system.CfgService;
import cn.enilu.flash.service.system.FileService;
import cn.enilu.flash.service.system.LogObjectHolder;
import cn.enilu.flash.utils.Maps;
import cn.enilu.flash.utils.StringUtil;
import cn.enilu.flash.utils.factory.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * CfgController
 *
 * @author enilu
 * @version 2018/11/17 0017
 */
@RestController
@RequestMapping("/buy")
public class BuyController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CfgService cfgService;
    @Autowired
    private BuyService buyService;
    @Autowired
    private FileService fileService;

    /**
     * 查询列表
     */
    @GetMapping(value = "/list")
    @RequiresPermissions(value = {"/buy"})
    public Object list(@RequestParam(required = false) String buyName,@RequestParam(required = false) String buyCode,
                       @RequestParam(required = false) Integer queryCode) {
        Page<Buy> pageQuery = new PageFactory<Buy>().defaultPage();
        if (queryCode != null) {
            pageQuery.addFilter(SearchFilter.build("isCheck", SearchFilter.Operator.EQ, queryCode));
        }
        if(StringUtil.isNotEmpty(buyName)){
            pageQuery.addFilter(SearchFilter.build("buyName", SearchFilter.Operator.LIKE, buyName));
        }
        if(StringUtil.isNotEmpty(buyCode)){
            pageQuery.addFilter(SearchFilter.build("buyCode", SearchFilter.Operator.LIKE, buyCode));
        }
        pageQuery = buyService.queryPage(pageQuery);
        return Rets.success(pageQuery);
    }

    /**
     * 导出参数列表
     *
     * @param cfgName
     * @param cfgValue
     * @return
     */
    @GetMapping(value = "/export")
    @RequiresPermissions(value = {Permission.CFG})
    public Object export(@RequestParam(required = false) String cfgName, @RequestParam(required = false) String cfgValue) {
        Page<Cfg> page = new PageFactory<Cfg>().defaultPage();
        if (StringUtil.isNotEmpty(cfgName)) {
            page.addFilter(SearchFilter.build("cfgName", SearchFilter.Operator.LIKE, cfgName));
        }
        if (StringUtil.isNotEmpty(cfgValue)) {
            page.addFilter(SearchFilter.build("cfgValue", SearchFilter.Operator.LIKE, cfgValue));
        }
        page = cfgService.queryPage(page);
        FileInfo fileInfo = fileService.createExcel("templates/config.xlsx", "系统参数.xlsx", Maps.newHashMap("list", page.getRecords()));
        return Rets.success(fileInfo);
    }

    @PostMapping(value = "/transfer")
    @BussinessLog(value = "识别购买信息", key = "buyInfo")
    @RequiresPermissions(value = {"/buy/transfer"})
    public Object transfer(@RequestBody @Valid BuyData buyData) {
        List<Buy>  buyList =  buyService.transfer(buyData);
        return Rets.success(buyList);
    }

    @PostMapping
    @BussinessLog(value = "新增", key = "buyName")
    @RequiresPermissions(value = {"/buy/add"})
    public Object add(@RequestBody  List<Buy>  buyList) {
        buyService.add(buyList);
        return Rets.success();
    }

    @PutMapping
    @BussinessLog(value = "编辑", key = "buyName")
    @RequiresPermissions(value = {"/buy/update"})
    public Object update(@RequestBody @Valid Buy buy) {
        Buy old = buyService.get(buy.getId());
        LogObjectHolder.me().set(old);
        buyService.saveOrUpdate(buy);
        return Rets.success();
    }

    @DeleteMapping
    @BussinessLog(value = "删除参数", key = "id")
    @RequiresPermissions(value = {"/buy/delete"})
    public Object remove(@RequestParam Long id) {
        logger.info("id:{}", id);
        if (id == null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        if(id<9){
            return Rets.failure("禁止删除初始化参数");
        }
        buyService.delete(id);
        return Rets.success();
    }
}
