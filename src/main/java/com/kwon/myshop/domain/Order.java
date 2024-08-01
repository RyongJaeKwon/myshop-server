package com.kwon.myshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kwon.myshop.exception.OrderCancelException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"delivery", "member"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    private int totalPrice;

    public void cancel() {

        if (this.status == OrderStatus.CANCEL) {
            throw new OrderCancelException("이미 취소된 주문입니다");
        }

        if (this.delivery.getStatus() == DeliveryStatus.DELIVERY_IN_PROGRESS ||
                this.delivery.getStatus() == DeliveryStatus.DELIVERY_COMP) {
            throw new OrderCancelException("배송중이거나 배송완료된 주문은 취소할 수 없습니다");
        }

        this.status = OrderStatus.CANCEL;
    }

}
