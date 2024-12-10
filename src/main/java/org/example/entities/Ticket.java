package org.example.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_gen")
    @SequenceGenerator(name = "ticket_id_gen", sequenceName = "ticket_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private Client user;

    @Column(name = "carnivalrow", nullable = false)
    private String carnivalrow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

    public String getCarnivalrow() {
        return carnivalrow;
    }

    public void setCarnivalrow(String carnivalrow) {
        this.carnivalrow = carnivalrow;
    }

}