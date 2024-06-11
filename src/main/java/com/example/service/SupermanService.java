package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.Superman;
import com.example.mapper.SupermanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SupermanService {

    @Autowired
    private SupermanMapper supermanMapper;

    /**
     * 递归读取
     * 查询所有赛亚人
     * @return
     */
    public List<Superman> getAllSuperman() {
        // 查出所有的根节点：孙悟空、贝吉塔
        List<Superman> rootSupermanList = getRootSuperman();
        // 分别查出孙悟空和贝吉塔的孩子，并设置
        for (Superman superman : rootSupermanList) {            
            List<Superman> children = queryChildrenByParent(superman);
            superman.setChildren(children);
        }
        // 返回
        return rootSupermanList;
    }

    /**
     * 查出根节点（pid=0）
     * @return
     */
    private List<Superman> getRootSuperman() {
        LambdaQueryWrapper<Superman> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Superman::getPid, 0);
        return supermanMapper.selectList(wrapper);
    }

    /**
     * 递归查询孩子
     * @param parent
     * @return
     */
    private List<Superman> queryChildrenByParent(Superman parent) {
        // 准备查询条件query
        LambdaQueryWrapper<Superman> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Superman::getPid, parent.getId());
        // 查出孩子
        List<Superman> children = supermanMapper.selectList(wrapper);
        // 查出每个孩子的孩子，并设置
        for (Superman child : children) {
            List<Superman> grandChildren = queryChildrenByParent(child);// 递归
            child.setChildren(grandChildren);
        }
        return children;
    }



    /**
     * 查询所有赛亚人
     * @return
     */
    public List<Superman> getAllSupermanV2() {
        // 用来存储最终的结果
        List<Superman> list = new ArrayList<>();
        // 先查出全部数据
        List<Superman> data = supermanMapper.selectList(null);

        // 双层for循环完成数据组装
        for (Superman left : data) {
            for (Superman right : data) {
                // 如果右边是左边的孩子，就设置进去
                if(left.getId() == right.getPid()){
                    left.getChildren().add(right);
                }
            }
            // 只把第1级加到list
            if(left.getPid() == 0){
                list.add(left);
            }
        }

        return list;
    }


    public List<Superman> getAllSupermanV3() {
        // 用来存储最终的结果
        List<Superman> list = new ArrayList<>();
        // 先查出全部数据
        List<Superman> data = supermanMapper.selectList(null);
        Map<Integer, Superman> map = data.stream().collect(Collectors.toMap(Superman::getId, Function.identity()));
        for (Superman item : data) {
            if (item.getPid() == 0) {
                list.add(item);
            } else {
                Superman parent = map.get(item.getPid());
                parent.getChildren().add(item);
            }
        }
        return list;
    }

    /**
     * 查询pid对应的下级内容
     * @param pid
     * @return
     */
    public List<Superman> getSupermanByPid(Integer pid) {
        LambdaQueryWrapper<Superman> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Superman::getPid, pid);
        return supermanMapper.selectList(wrapper);
    }
}
