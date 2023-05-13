package cn.enilu.flash.service;

import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 2019/6/29 22:28
 */
public interface InsertService<T, ID> {

    /**
     * 添加一条数据
     *
     * @param record 要添加的数据
     * @return 添加后生成的主键
     */
    T insert(T record);

    /**
     * 添加一条数据
     *
     * @param records 要添加的数据
     * @return 添加后生成的主键
     */
    List<T> batchInsert(List<T> records);
}
