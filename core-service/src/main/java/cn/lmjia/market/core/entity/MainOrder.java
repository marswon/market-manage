package cn.lmjia.market.core.entity;

import cn.lmjia.market.core.entity.record.MainOrderRecord;
import cn.lmjia.market.core.entity.support.Address;
import lombok.Getter;
import lombok.Setter;
import me.jiangcai.payment.PayableOrder;
import me.jiangcai.payment.entity.PayOrder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author CJ
 */
@Entity
@Setter
@Getter
public class MainOrder implements PayableOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 原始记录
     */
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private MainOrderRecord record;
    /**
     * 谁下的单
     */
    @ManyToOne
    private Login orderBy;
    /**
     * 谁买的
     */
    @ManyToOne
    private Customer customer;
    /**
     * 谁推荐的
     */
    @ManyToOne
    private Login recommendBy;

    private Address installAddress;

    /**
     * 具体的产品
     */
    @ManyToOne
    private MainGood good;
    private int amount;
    /**
     * 按揭识别码
     */
    @Column(length = 32)
    private String mortgageIdentifier;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime orderTime;

    /**
     * 成功支付的支付订单
     */
    @ManyToOne
    private PayOrder payOrder;
    @Column(columnDefinition = "timestamp")
    private LocalDateTime payTime;
    /**
     * 支付是否已完成
     */
    private boolean pay;

    /**
     * 创建下单记录
     */
    public void makeRecord() {
        if (record != null)
            throw new IllegalStateException("I really have a record!");
        record = new MainOrderRecord();
        record.setOrderTime(orderTime);
        record.setAge(LocalDate.now().getYear() - customer.getBirthYear());
        record.setAmount(amount);
        record.setGender(customer.getGender());
        record.setInstallAddress(installAddress);
        record.setMobile(customer.getMobile());
        record.setMortgageIdentifier(mortgageIdentifier);
        record.setName(customer.getName());
        record.setProductName(good.getProduct().getName());
        record.setProductType(good.getProduct().getCode());
        record.setRecommendByMobile(recommendBy.getLoginName());
    }

    @Override
    public Serializable getPayableOrderId() {
        return "main-" + id;
    }

    @Override
    public BigDecimal getOrderDueAmount() {
        return good.getTotalPrice().multiply(BigDecimal.valueOf(amount));
    }

    @Override
    public String getOrderProductName() {
        return good.getProduct().getName();
    }
}