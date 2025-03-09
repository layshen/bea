package com.epiboly.bea2.http.api;

import androidx.annotation.Keep;

import com.hjq.http.config.IRequestApi;

/**
 * @author vemao
 * @time 2023/1/30
 * @describe
 */
public class QueryProductApi implements IRequestApi {
    @Override
    public String getApi() {
        return "product/queryProductList";
    }

    private String token;

    private Integer pageNum;

    private Integer pageSize = 20;

    public Integer getPageNum() {
        return pageNum;
    }

    public QueryProductApi setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public QueryProductApi setToken(String token) {
        this.token = token;
        return this;
    }
    @Keep
    public static class Bean{
        private int finishedCount;//已经完成的任务数量
        private int totalCount;//总共需要完成的任务数量
        private int[] finishedSort;//记录完成任务的顺序

        public int getFinishedCount() {
            return finishedCount;
        }

        public void setFinishedCount(int finishedCount) {
            this.finishedCount = finishedCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int[] getFinishedSort() {
            return finishedSort;
        }

        public void setFinishedSort(int[] finishedSort) {
            this.finishedSort = finishedSort;
        }

        public boolean isFinishTask(int i) {
            if (getFinishedSort() == null || getFinishedSort().length == 0){
                return false;
            }
            for (int index : getFinishedSort()) {
                if (index == i){
                    return true;
                }
            }
            return false;
        }
    }
}
