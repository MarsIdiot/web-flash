package cn.enilu.flash.bean.entity.system;

import cn.enilu.flash.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Data
public class BuyData {
    @NotBlank(message = "购买原始信息")
    private String buyData;
    @NotBlank(message = "购买人编号不能为空")
    private String buyId;
    @NotBlank(message = "购买人名称不能为空")
    private String buyName;

}
