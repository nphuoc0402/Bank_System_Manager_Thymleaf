package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfers")
@Accessors(chain = true)
public class Transfer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id",referencedColumnName = "id")
    private Customer customerSend;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id",referencedColumnName = "id")
    private Customer customerRec;


    @Digits(integer = 9, fraction = 0)
    @Column(name = "transfer_amount", nullable= false)
    private BigDecimal transferAmount;

    @Column(nullable= false)
    private int fees;

    @Digits(integer = 9, fraction = 0)
    @Column(name = "fees_amount", nullable= false)
    private BigDecimal feesAmount;

    @Digits(integer = 9, fraction = 0)
    @Column(name = "transaction_amount", nullable= false)
    private BigDecimal transactionAmount;

    public Transfer(Customer customerSend, Customer customerRec, BigDecimal transferAmount, int fees, BigDecimal transactionAmount) {
        this.customerSend = customerSend;
        this.customerRec = customerRec;
        this.transferAmount = transferAmount;
        this.fees = fees;
        this.transactionAmount = transactionAmount;
    }

}
