package com.epiboly.bea.http.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class HttpList2Data<T> extends HttpData<HttpList2Data.ListBean<T>> {

    public static class ListBean<T> {

        /** 当前页码 */
        private int pageNum;
        /** 页大小 */
        private int pageSize;
        /** 总数量 */
        private int total;
        /** 数据 */
        private List<T> list;

        public int getTotal() {
            return total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public List<T> getList() {
            return list;
        }
    }
}