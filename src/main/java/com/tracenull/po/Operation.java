package com.tracenull.po;

import com.tracenull.eu.OperationType;
import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@NotEmpty
@ToString
public class Operation {
    @Id
    @GeneratedValue
    private String id;
    private OperationType opType;
    private String opBusinessName;
    private String opBusinessId;
    private long opTime;
    private String opUser;
}
