package com.pbs.ams.web.model;

import java.io.Serializable;

public class AmsProductAccount implements Serializable {
    /**
     * 编号
     *
     * @mbg.generated
     */
    private Long productAccountId;

    /**
     * 产品ID
     *
     * @mbg.generated
     */
    private Long productId;

    /**
     * 账号ID
     *
     * @mbg.generated
     */
    private Long stockAccountId;

    private static final long serialVersionUID = 1L;

    public Long getProductAccountId() {
        return productAccountId;
    }

    public void setProductAccountId(Long productAccountId) {
        this.productAccountId = productAccountId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStockAccountId() {
        return stockAccountId;
    }

    public void setStockAccountId(Long stockAccountId) {
        this.stockAccountId = stockAccountId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", productAccountId=").append(productAccountId);
        sb.append(", productId=").append(productId);
        sb.append(", stockAccountId=").append(stockAccountId);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AmsProductAccount other = (AmsProductAccount) that;
        return (this.getProductAccountId() == null ? other.getProductAccountId() == null : this.getProductAccountId().equals(other.getProductAccountId()))
            && (this.getProductId() == null ? other.getProductId() == null : this.getProductId().equals(other.getProductId()))
            && (this.getStockAccountId() == null ? other.getStockAccountId() == null : this.getStockAccountId().equals(other.getStockAccountId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProductAccountId() == null) ? 0 : getProductAccountId().hashCode());
        result = prime * result + ((getProductId() == null) ? 0 : getProductId().hashCode());
        result = prime * result + ((getStockAccountId() == null) ? 0 : getStockAccountId().hashCode());
        return result;
    }
}