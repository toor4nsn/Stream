package com.common;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * 我们目前的标准分库分表策略为16库1024表
 * 分库的策略: (shardingValue%1024)/64
 * 分表的策略: shardingValue%1024
 *
 * aops000：aops_car0000 -> aops_car0063
 * aops001：aops_car0064 -> aops_car0127
 * aops002：aops_car0128 -> aops_car0191
 * aops002：aops_car0192 -> aops_car0255
 * ...
 * aops015：aops_car0960 -> aops_car1023
 *
 */

/**
 * 查询语句：
 * set @aopsid = '28060361';
 * set @db = floor(mod(@aopsid,1024)/64), @tb = mod(@aopsid,1024);
 * select @db, @tb;
 * set @sql = concat("select * from aops", lpad(@db, 3, 0), ".aops_car", lpad(@tb, 4, 0), " where STATUS=0 and aopsid=", @aopsid, ";");
 * prepare stmt from @sql;
 * execute stmt;
 *
 */

public class Aops_table {

    private static BufferedWriter sqlWriter = null;

    private static void writeSql(String sql, String filePath) {
        try {
            if (null == sqlWriter) {
                sqlWriter = new BufferedWriter(new FileWriter(filePath));
            }
            sqlWriter.write(sql + "\n");

            sqlWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createSql(String filePath){
        for (int i = 0; i < 16; i++) {
            StringBuffer sql = new StringBuffer();

            int begin = i * 64;
            int end = (i + 1) * 64;

            for (int j = begin; j < end; j++) {
                sql.append(" CREATE TABLE aops" + getIndexStr(i, 3));
                sql.append(".aops_lottery_activity_flow" + getIndexStr(j, 4));
                sql.append( " (`id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '主键',");
                sql.append( "   `aops_id` bigint(20) NOT NULL COMMENT '用户id',");
                sql.append( "   `car_id` bigint(20) DEFAULT NULL COMMENT '车辆id',");
                sql.append( "   `activity_id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '活动id',");
                sql.append( "   `relation_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '关联ID（邀请码或批量邀请码）',");
                sql.append( "   `task_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务id',");
                sql.append( "   `obm_task_id` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '节点后台任务id',");
                sql.append( "   `prize_name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '奖励名称',");
                sql.append( "   `prize_bg_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '奖励图片',");
                sql.append( "   `item_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权益id',");
                sql.append( "   `prize_type` int(5) DEFAULT NULL COMMENT '奖品类型',");
                sql.append( "   `department_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '机构编码',");
                sql.append( "   `source` int(11) DEFAULT NULL COMMENT '来源',");
                sql.append( "   `prize_status` tinyint(2) DEFAULT NULL COMMENT '奖励状态（0：未发放，1：已发放）',");
                sql.append( "   `status` tinyint(2) DEFAULT '0' COMMENT '状态（0：正常，1：删除）',");
                sql.append( "   `created_date` datetime DEFAULT NULL COMMENT '创建时间',");
                sql.append( "   `updated_date` datetime DEFAULT NULL COMMENT '更新时间',");
                sql.append( "   PRIMARY KEY (`id`),");
                sql.append( "   KEY `index_aopsid` (`aops_id`,`activity_id`) USING BTREE");
                sql.append( " ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='抽奖活动流水';");
                sql.append( "\r\n");
            }

            System.out.println(sql.toString());
            writeSql(sql.toString(), filePath);

        }
    }

    private static void alterTable(String filePath){
        for (int i = 0; i < 16; i++) {
            StringBuffer sql = new StringBuffer();

            int begin = i * 64;
            int end = (i + 1) * 64;
            for (int j = begin; j < end; j++) {
                sql.append(" ALTER TABLE aops" + getIndexStr(i, 3));
                sql.append(".aops_car_notification" + getIndexStr(j, 4));
                sql.append(" ADD COLUMN `car_no_flag`  int(11) NULL DEFAULT 0 COMMENT '车牌号留存标识（0：不是，1：是）';");
                sql.append( "\r\n");
            }

            System.out.println(sql.toString());
            writeSql(sql.toString(), filePath);

        }
    }

    private static void dropTable(String filePath){
        for (int i = 0; i < 16; i++) {
            StringBuffer sql = new StringBuffer();

            int begin = i * 64;
            int end = (i + 1) * 64;
            for (int j = begin; j < end; j++) {
                sql.append(" DROP TABLE aops" + getIndexStr(i, 3));
                sql.append(".aops_lottery_activity_flow" + getIndexStr(j, 4));
                sql.append( " ;");
                sql.append( "\r\n");
            }

            System.out.println(sql.toString());
            writeSql(sql.toString(), filePath);

        }
    }

    public static void main(String[] args) {
//        String createSqlPath = "D:/aops_lottery_activity_flow_create.sql";
//        createSql(createSqlPath);

//        String sqlPath = "D:/aops_car_notification_alter.sql";
//        alterTable(sqlPath);

//        String sqlPath = "D:/aops_lottery_activity_flow_drop.sql";
//        dropTable(sqlPath);
        String sqlPath = "D:/dml_rtp_todo_user_record.sql";
        updateTable(sqlPath);
    }

    private static String getIndexStr(int num, int leng) {
        StringBuffer sb = new StringBuffer();

        String numStr = num + "";

        int size = leng - numStr.length();
        for (int i = 0; i < size; i++) {
            sb.append("0");
        }
        sb.append(num);
        return sb.toString();
    }


    private static void updateTable(String filePath){
        for (int i = 0; i < 16; i++) {
            StringBuffer sql = new StringBuffer();

            int begin = i * 64;
            int end = (i + 1) * 64;
            for (int j = begin; j < end; j++) {
                sql.append("update aops" + getIndexStr(i, 3));
                sql.append(".rtp_todo_user_record" + getIndexStr(j, 4));
                sql.append(" set `status`=0 where bus_type = 'td_annual' and `status` = 1;");
                sql.append( "\r\n");
            }

            System.out.println(sql.toString());
            writeSql(sql.toString(), filePath);

        }
    }
}