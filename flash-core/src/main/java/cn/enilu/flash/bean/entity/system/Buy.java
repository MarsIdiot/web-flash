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
@Entity(name = "t_sys_buy")
@Table(appliesTo = "t_sys_buy", comment = "购买信息表")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Buy extends BaseEntity {
    /**
     * `is_check` tinyint(1) DEFAULT 0 COMMENT '是否审核：0待审核 1已审核',
     *   `is_delete` tinyint(1) DEFAULT 0 COMMENT '是否删除：0未删除 1已删除',
     *   `buy_code` int(11) DEFAULT NULL COMMENT '购买编码',
     *   `buy_count` int(11) DEFAULT NULL COMMENT '购买金额',
     *   `buy_id` varchar(64) DEFAULT NULL COMMENT '购买人编号',
     *   `buy_name` varchar(64) DEFAULT NULL COMMENT '购买人名称',
     */


    @Column(name = "is_check", columnDefinition = "tinyint(1)  COMMENT '是否审核：0待审核 1已审核'")
    private boolean isCheck;
    @Column(name = "is_delete", columnDefinition = "tinyint(1) COMMENT '是否删除：0未删除 1已删除'")
    private boolean isDelete;
    @Column(name = "buy_code", columnDefinition = "int(11)  COMMENT '购买编码'")
    private Integer buyCode;
    @Column(name = "buy_count", columnDefinition = "int(11)  COMMENT '购买金额'")
    private Integer buyCount;
    @Column(name = "buy_id", columnDefinition = "varchar(64) COMMENT '购买人编号'")
    private String buyId;
    @Column(name = "buy_name", columnDefinition = "varchar(64)  COMMENT '购买人名称'")
    private String buyName;


    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean check) {
        isCheck = check;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean delete) {
        isDelete = delete;
    }
}
