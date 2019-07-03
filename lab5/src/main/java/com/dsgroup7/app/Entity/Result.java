package com.dsgroup7.app.Entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Result {
    private int id;
    private Integer userId;
    private String initiator;
    private String success;
    private Double paid;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "initiator", nullable = true, length = 5)
    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    @Basic
    @Column(name = "success", nullable = true)
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Basic
    @Column(name = "paid", nullable = true, precision = 0)
    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return id == result.id &&
                Objects.equals(userId, result.userId) &&
                Objects.equals(initiator, result.initiator) &&
                Objects.equals(success, result.success) &&
                Objects.equals(paid, result.paid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, initiator, success, paid);
    }
}
